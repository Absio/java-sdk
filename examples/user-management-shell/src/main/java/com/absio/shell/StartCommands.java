package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.provider.KeyFileNotFoundException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("1. Getting Started Commands")
public class StartCommands {
    @ShellMethod("Initializes the shell with the OfsProvider class. This sources all data from the local encrypted file system.")
    public String initOfs(String rootDirectory, boolean partitionByUser) throws SQLException {
        AbsioProvider.INSTANCE.initializeOfsProvider(Paths.get(rootDirectory), partitionByUser);
        return "OfsProvider Session initialized -> " + joinValues(rootDirectory, String.valueOf(partitionByUser));
    }

    @ShellMethod("Initializes the shell with the ServerProvider class. This all data from the Absio Broker application and performs no file access.")
    public String initServer(String url, String apiKey, @ShellOption(defaultValue = ShellOption.NULL) String appName) throws NoSuchPaddingException, NoSuchAlgorithmException {
        AbsioProvider.INSTANCE.initializeServerProvider(url, apiKey, appName);
        return "ServerProvider Session initialized -> " + joinValues(url, apiKey, appName);
    }

    @ShellMethod("Initializes the shell with the ServerCacheOfsProvider class. This sources all data from the Absio Broker application and caches data locally in a local encrypted file system.")
    public String initServerCacheOfs(String url, String apiKey, @ShellOption(defaultValue = ShellOption.NULL) String appName, String rootDirectory, boolean partitionByUser) throws NoSuchPaddingException, NoSuchAlgorithmException, SQLException {
        AbsioProvider.INSTANCE.initializeServerCacheOfsProvider(url, apiKey, appName, Paths.get(rootDirectory), partitionByUser);
        return "ServerCacheOfsProvider Session initialized -> " + joinValues(url, apiKey, appName, rootDirectory, String.valueOf(partitionByUser));
    }

    public Availability initializedAvailabilityCheck() throws IllegalAccessException {
        return AbsioProvider.INSTANCE.isInitialized()
               ? Availability.available()
               : Availability.unavailable("The session must be initialized.");

    }

    private String joinValues(CharSequence... elements) {
        return String.join(" : ", elements);
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Log in to the Absio Broker server.")
    public String login(String userId, @ShellOption(defaultValue = ShellOption.NULL) String password, String passphrase) throws BrokerException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, IllegalAccessException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, InterruptedException, SQLException, KeyFileNotFoundException {
        AbsioProvider.INSTANCE.logIn(UUID.fromString(userId), password, passphrase);
        return "Login successful -> " + userId;
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Registers a new user.")
    public String register(String password, @ShellOption(defaultValue = ShellOption.NULL) String passphrase, @ShellOption(defaultValue = ShellOption.NULL) String reminder, @ShellOption(defaultValue = ShellOption.NULL) String keyFileOutputFile) throws IllegalAccessException, NoSuchAlgorithmException, BrokerException, InvalidKeyException, SignatureException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IOException, IllegalBlockSizeException, InterruptedException, SQLException {
        Path keyFile = null;

        if (keyFileOutputFile != null) {
            keyFile = Files.createFile(Paths.get(keyFileOutputFile));
        }

        byte[] keyFileBytes = AbsioProvider.INSTANCE.register(password, passphrase, reminder);

        if (keyFile != null) {
            Files.write(keyFile, keyFileBytes);
        }

        return "User registered successfully -> " + AbsioProvider.INSTANCE.getUserId().toString();
    }
}
