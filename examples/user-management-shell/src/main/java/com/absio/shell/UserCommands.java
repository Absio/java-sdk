package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.util.FileUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("2. User Account Commands")
public class UserCommands {
    public Availability initializedAvailabilityCheck() {
        return AbsioServerProvider.INSTANCE.isInitialized() ? Availability.available() : Availability.unavailable("The session must be initialized.");
    }

    public Availability authenticatedAvailabilityCheck() {
        return AbsioServerProvider.INSTANCE.isAuthenticated() ? Availability.available() : Availability.unavailable("The session must be authenticated.");
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Changes a user's account credentials.")
    public String changeCredentials(String password, @ShellOption(defaultValue = ShellOption.NULL) String passphrase, @ShellOption(defaultValue = ShellOption.NULL) String reminder) throws IOException, NoSuchAlgorithmException, BrokerException, InvalidKeyException, InterruptedException, IllegalAccessException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException {
        AbsioServerProvider.INSTANCE.changeCredentials(password, passphrase, reminder);
        return "Credentials changed successfully";
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Get reminder.")
    public String getReminder(String userId) throws InterruptedException, IOException, BrokerException, IllegalAccessException {
        return "Reminder for " + userId + " -> " + AbsioServerProvider.INSTANCE.getReminder(UUID.fromString(userId));
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Delete the user.")
    public String deleteUser() throws InterruptedException, IOException, BrokerException, IllegalAccessException {
        UUID userId = AbsioServerProvider.INSTANCE.getUserId();
        AbsioServerProvider.INSTANCE.deleteUser();
        return "User " + userId + " deleted successfully";
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Logs out of the Absio session.")
    public String logout() {
        AbsioServerProvider.INSTANCE.logOut();
        return "Logged out";
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Need to sync account.")
    public String needToSyncAccount(String userId, String keyFile) throws IOException, InterruptedException, BrokerException, NoSuchAlgorithmException, IllegalAccessException {
        boolean needToSync = AbsioServerProvider.INSTANCE.needToSyncAccount(UUID.fromString(userId), FileUtils.readBytesFromFile(keyFile));
        return "Need to synchronize key file -> " + needToSync;
    }
}