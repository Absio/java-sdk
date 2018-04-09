package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.crypto.key.IndexedECPublicKey;
import com.absio.crypto.key.KeyType;
import com.absio.file.FileExistsException;
import com.absio.provider.ServerProvider;
import com.absio.util.FileUtils;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.Availability;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.UUID;

@ShellComponent
public class UserCommands {
    private ServerProvider provider = new ServerProvider();

    public UserCommands() throws NoSuchAlgorithmException {
    }

    private static String publicKeyToString(IndexedECPublicKey publicKey) {
        return String.format("%s Public key %s : %s : %b : %s", publicKey.getKeyType(), publicKey.getIndex(), publicKey.isActive() ? "active" : "inactive", publicKey.getAlgorithm(), DatatypeConverter.printHexBinary(publicKey.getEncoded()));
    }

    public Availability authenticatedAvailabilityCheck() {
        return provider.isAuthenticated() ? Availability.available() : Availability.unavailable("The session must be authenticated.");
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Changes a user's account credentials.")
    public String changeCredentials(String password, @ShellOption(defaultValue = ShellOption.NULL) String passphrase, @ShellOption(defaultValue = ShellOption.NULL) String reminder) throws IOException, NoSuchAlgorithmException, BrokerException, InvalidKeyException, InterruptedException, IllegalAccessException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException {
        provider.changeCredentials(password, passphrase, reminder);
        return "Credentials changed successfully";
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Delete the user.")
    public String deleteUser() throws InterruptedException, IOException, BrokerException, IllegalAccessException {
        UUID userId = provider.getUserId();
        provider.deleteUser();
        return "User " + userId + " deleted successfully";
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Retrieve a user's public key by type and index.")
    public String getPublicKeyByIndex(String userId, String keyType, int index) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        IndexedECPublicKey publicKey = provider.getPublicKeyByIndex(UUID.fromString(userId), KeyType.findByName(keyType), index);

        return publicKeyToString(publicKey);
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Retrieve a user's latest active key by type.")
    public String getPublicKeyLatestActive(String userId, String keyType) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        IndexedECPublicKey publicKey = provider.getPublicKeyLatestActive(UUID.fromString(userId), KeyType.findByName(keyType));

        return publicKeyToString(publicKey);
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Retrieves a list of public keys filterable by user, type, and index.")
    public String getPublicKeyList(@ShellOption(defaultValue = ShellOption.NULL) String userId, @ShellOption(defaultValue = ShellOption.NULL) String keyType, @ShellOption(defaultValue = ShellOption.NULL) Integer index) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        UUID userUUID = userId != null ? UUID.fromString(userId) : null;
        KeyType type = keyType != null ? KeyType.findByName(keyType) : null;
        List<IndexedECPublicKey> publicKeyList = provider.getPublicKeyList(userUUID, type, index);

        StringBuilder publicKeys = new StringBuilder();
        for (IndexedECPublicKey publicKey :
                publicKeyList) {
            publicKeys.append(publicKeyToString(publicKey));
            publicKeys.append("\n");
        }

        return publicKeys.toString();
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Get reminder.")
    public String getReminder(String userId) throws InterruptedException, IOException, BrokerException, IllegalAccessException {
        return "Reminder for " + userId + " -> " + provider.getReminder(UUID.fromString(userId));
    }

    @ShellMethod("Initializes the Absio shell.")
    public String initialize(String url, String apiKey, @ShellOption(defaultValue = ShellOption.NULL) String appName) throws NoSuchPaddingException, NoSuchAlgorithmException {
        provider.initialize(url, apiKey, appName);
        return "Session initialized -> " + url + " : " + apiKey + " : " + appName;
    }

    public Availability initializedAvailabilityCheck() {
        return provider.isInitialized()
               ? Availability.available()
               : Availability.unavailable("The session must be initialized.");
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Log in to the Absio Broker server.")
    public String login(String userId, @ShellOption(defaultValue = ShellOption.NULL) String password, String passphrase) throws BrokerException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, IllegalAccessException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, InterruptedException {
        provider.logIn(UUID.fromString(userId), password, passphrase);
        return "Login successful -> " + userId;
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Logs out of the Absio session.")
    public String logout() {
        provider.logOut();
        return "Logged out";
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Need to sync account.")
    public String needToSyncAccount(String userId, String keyFile) throws IOException, InterruptedException, BrokerException, NoSuchAlgorithmException, IllegalAccessException {
        boolean needToSync = provider.needToSyncAccount(UUID.fromString(userId), FileUtils.readBytesFromFile(keyFile));
        return "Need to synchronize key file -> " + needToSync;
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Registers a new user.")
    public String register(String password, @ShellOption(defaultValue = ShellOption.NULL) String passphrase, @ShellOption(defaultValue = ShellOption.NULL) String reminder, @ShellOption(defaultValue = ShellOption.NULL) String keyFileOutputFile) throws IllegalAccessException, NoSuchAlgorithmException, BrokerException, InvalidKeyException, SignatureException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IOException, IllegalBlockSizeException, InterruptedException, FileExistsException {
        File keyFile = null;

        if (keyFileOutputFile != null) {
            keyFile = FileUtils.createFile(keyFileOutputFile, false);
        }

        byte[] keyFileBytes = provider.register(password, passphrase, reminder);

        if (keyFile != null) {
            FileUtils.writeToFile(keyFile, keyFileBytes);
        }

        return "User registered successfully -> " + provider.getUserId().toString();
    }

    @Component
    public class CustomPromptProvider implements PromptProvider {

        @Override
        public AttributedString getPrompt() {
            if (provider.isAuthenticated()) {
                return new AttributedString(provider.getUserId() + ":>",
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
            }
            else if (provider.isInitialized()) {
                return new AttributedString("absio" + ":>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
            }
            else {
                return new AttributedString("shell:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
            }
            //initialize https://apidev.absio.com 62883408-7ff0-49c4-a48e-f3146891b486 | login --user-id fa49c241-c208-40f4-8d85-58a2d27e42b4 --passphrase passphrase#2
        }
    }
}