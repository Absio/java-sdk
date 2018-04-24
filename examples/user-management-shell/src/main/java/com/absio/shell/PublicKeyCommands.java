package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.crypto.key.IndexedECPublicKey;
import com.absio.crypto.key.KeyType;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("3. Public Key Commands")
public class PublicKeyCommands {
    @ShellMethodAvailability("*")
    public Availability publicKeyMethodAvailabilityCheck() {
        return AbsioServerProvider.INSTANCE.isAuthenticated() ? Availability.available() : Availability.unavailable("The session must be authenticated.");
    }

    @ShellMethod("Retrieve a user's public key by type and index.")
    public String getPublicKeyByIndex(String userId, @ShellOption(help = "{Signing | Derivation}") KeyType keyType, int index) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        IndexedECPublicKey publicKey = AbsioServerProvider.INSTANCE.getPublicKeyByIndex(UUID.fromString(userId), keyType, index);

        return publicKeyToString(publicKey);
    }

    private static String publicKeyToString(IndexedECPublicKey publicKey) {
        return String.format("%s Public key %s : %s : %b : %s", publicKey.getKeyType(), publicKey.getIndex(), publicKey.isActive() ? "active" : "inactive", publicKey.getAlgorithm(), DatatypeConverter.printHexBinary(publicKey.getEncoded()));
    }

    @ShellMethod("Retrieve a user's latest active key by type.")
    public String getPublicKeyLatestActive(String userId, @ShellOption(help = "{Signing | Derivation}") KeyType keyType) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        IndexedECPublicKey publicKey = AbsioServerProvider.INSTANCE.getPublicKeyLatestActive(UUID.fromString(userId), keyType);

        return publicKeyToString(publicKey);
    }

    @ShellMethod("Retrieves a list of public keys filterable by user, type, and index.")
    public String getPublicKeyList(@ShellOption(defaultValue = ShellOption.NULL) String userId, @ShellOption(defaultValue = ShellOption.NULL, help = "{Signing | Derivation}") KeyType keyType, @ShellOption(defaultValue = ShellOption.NULL) Integer index) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        UUID userUUID = userId != null ? UUID.fromString(userId) : null;
        List<IndexedECPublicKey> publicKeyList = AbsioServerProvider.INSTANCE.getPublicKeyList(userUUID, keyType, index);

        StringBuilder publicKeys = new StringBuilder();
        for (IndexedECPublicKey publicKey : publicKeyList) {
            publicKeys.append(publicKeyToString(publicKey));
            publicKeys.append("\n");
        }

        return publicKeys.toString();
    }
}
