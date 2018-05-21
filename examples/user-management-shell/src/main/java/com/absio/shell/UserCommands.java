package com.absio.shell;

import com.absio.broker.BrokerException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("2. User Account Commands")
public class UserCommands {
    public Availability authenticatedAvailabilityCheck() throws IllegalAccessException {
        return AbsioProvider.INSTANCE.isAuthenticated() ? Availability.available() : Availability.unavailable("The session must be authenticated.");
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Changes a user's account credentials.")
    public String changeCredentials(String password, @ShellOption(defaultValue = ShellOption.NULL) String passphrase, @ShellOption(defaultValue = ShellOption.NULL) String reminder) throws IOException, NoSuchAlgorithmException, BrokerException, InvalidKeyException, InterruptedException, IllegalAccessException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException {
        AbsioProvider.INSTANCE.changeCredentials(password, passphrase, reminder);
        return "Credentials changed successfully";
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Delete the user.")
    public String deleteUser() throws InterruptedException, IOException, BrokerException, IllegalAccessException, SQLException, NoSuchAlgorithmException {
        UUID userId = AbsioProvider.INSTANCE.getUserId();
        AbsioProvider.INSTANCE.deleteUser();
        return "User " + userId + " deleted successfully";
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Get reminder.")
    public String getReminder(@ShellOption(defaultValue = ShellOption.NULL) String userId) throws InterruptedException, IOException, BrokerException, IllegalAccessException {
        UUID userUUID;
        if (userId == null) {
            userUUID = AbsioProvider.INSTANCE.getUserId();
        }
        else {
            userUUID = UUID.fromString(userId);
        }
        return "Reminder for " + userUUID.toString() + " -> " + AbsioProvider.INSTANCE.getReminder(userUUID);
    }

    public Availability initializedAvailabilityCheck() throws IllegalAccessException {
        return AbsioProvider.INSTANCE.isInitialized() ? Availability.available() : Availability.unavailable("The session must be initialized.");
    }

    @ShellMethodAvailability("authenticatedAvailabilityCheck")
    @ShellMethod("Logs out of the Absio session.")
    public String logout() throws SQLException, IllegalAccessException {
        AbsioProvider.INSTANCE.logOut();
        return "Logged out";
    }

    @ShellMethodAvailability("initializedAvailabilityCheck")
    @ShellMethod("Need to sync account.")
    public String needToSyncAccount(String userId, String keyFile) throws IOException, InterruptedException, BrokerException, NoSuchAlgorithmException, IllegalAccessException {
        boolean needToSync = AbsioProvider.INSTANCE.needToSyncAccount(UUID.fromString(userId), keyFile);
        return "Need to synchronize key file -> " + needToSync;
    }
}