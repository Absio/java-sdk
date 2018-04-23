package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.file.FileExistsException;
import com.absio.util.FileUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("1. Getting Started Commands")
public class StartCommands {
    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Log in to the Absio Broker server.")
    public String login(String userId, @ShellOption(defaultValue = ShellOption.NULL) String password, String passphrase) throws BrokerException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, IllegalAccessException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, InterruptedException {
        AbsioServerProvider.INSTANCE.logIn(UUID.fromString(userId), password, passphrase);
        return "Login successful -> " + userId;
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Registers a new user.")
    public String register(String password, @ShellOption(defaultValue = ShellOption.NULL) String passphrase, @ShellOption(defaultValue = ShellOption.NULL) String reminder, @ShellOption(defaultValue = ShellOption.NULL) String keyFileOutputFile) throws IllegalAccessException, NoSuchAlgorithmException, BrokerException, InvalidKeyException, SignatureException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IOException, IllegalBlockSizeException, InterruptedException, FileExistsException {
        File keyFile = null;

        if (keyFileOutputFile != null) {
            keyFile = FileUtils.createFile(keyFileOutputFile, false);
        }

        byte[] keyFileBytes = AbsioServerProvider.INSTANCE.register(password, passphrase, reminder);

        if (keyFile != null) {
            FileUtils.writeToFile(keyFile, keyFileBytes);
        }

        return "User registered successfully -> " + AbsioServerProvider.INSTANCE.getUserId().toString();
    }

    @ShellMethod("Initializes the Absio shell.")
    public String initialize(String url, String apiKey, @ShellOption(defaultValue = ShellOption.NULL) String appName) throws NoSuchPaddingException, NoSuchAlgorithmException {
        AbsioServerProvider.INSTANCE.initialize(url, apiKey, appName);
        return "Session initialized -> " + url + " : " + apiKey + " : " + appName;
    }

    public Availability initializedAvailabilityCheck() {
        return AbsioServerProvider.INSTANCE.isInitialized()
               ? Availability.available()
               : Availability.unavailable("The session must be initialized.");

    }
}
