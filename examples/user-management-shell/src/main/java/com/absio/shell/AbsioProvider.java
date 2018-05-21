package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.broker.mapper.ContainerInfo;
import com.absio.broker.mapper.EventActionType;
import com.absio.broker.mapper.EventPackage;
import com.absio.broker.mapper.EventType;
import com.absio.container.Access;
import com.absio.container.Container;
import com.absio.container.Metadata;
import com.absio.crypto.key.IndexedECPublicKey;
import com.absio.crypto.key.KeyType;
import com.absio.file.mapper.ContainerDbInfo;
import com.absio.provider.*;
import org.threeten.bp.ZonedDateTime;

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
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

class AbsioProvider {
    static AbsioProvider INSTANCE;

    static {
        try {
            INSTANCE = new AbsioProvider();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private OfsProvider ofsProvider;
    private ProviderType providerType;
    private ServerCacheOfsProvider serverCacheOfsProvider;
    private ServerProvider serverProvider;

    private AbsioProvider() throws NoSuchAlgorithmException {
        serverProvider = new ServerProvider();
        ofsProvider = new OfsProvider();
        serverCacheOfsProvider = new ServerCacheOfsProvider();
        providerType = ProviderType.SERVER_CACHE_OFS;
    }

    void changeCredentials(String password, String passphrase, String reminder) throws IOException, NoSuchAlgorithmException, BrokerException, InvalidKeyException, InterruptedException, IllegalAccessException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException {
        switch (providerType) {
            case SERVER:
                serverProvider.changeCredentials(password, passphrase, reminder);
                break;
            case OFS:
                ofsProvider.changeCredentials(password, passphrase);
                break;
            case SERVER_CACHE_OFS:
                serverCacheOfsProvider.changeCredentials(password, passphrase, reminder);
                break;
            default:
                throw createUnsupportedByProviderException("changeCredentials");
        }
    }

    void createOrUpdateContainer(Container container) throws Exception {
        switch (providerType) {
            case SERVER:
                serverProvider.createOrUpdate(container);
                break;
            case OFS:
                ofsProvider.createOrUpdate(container);
                break;
            case SERVER_CACHE_OFS:
                serverCacheOfsProvider.createOrUpdate(container);
                break;
            default:
                throw createUnsupportedByProviderException("createOrUpdateContainer");
        }
    }

    private IllegalAccessException createUnsupportedByProviderException(String method) {
        return new IllegalAccessException(String.format("The command %s is not supported by the provider type %s", method, providerType));
    }

    void deleteContainer(UUID id) throws InterruptedException, BrokerException, IllegalAccessException, IOException, SQLException {
        switch (providerType) {
            case SERVER:
                serverProvider.delete(id);
                break;
            case OFS:
                ofsProvider.delete(id);
                break;
            case SERVER_CACHE_OFS:
                serverCacheOfsProvider.delete(id);
                break;
            default:
                throw createUnsupportedByProviderException("deleteUser");
        }
    }

    void deleteUser() throws IllegalAccessException, InterruptedException, NoSuchAlgorithmException, IOException, SQLException, BrokerException {
        switch (providerType) {
            case SERVER:
                serverProvider.deleteUser();
                break;
            case OFS:
                ofsProvider.deleteUser();
                break;
            case SERVER_CACHE_OFS:
                serverCacheOfsProvider.deleteUser();
                break;
            default:
                throw createUnsupportedByProviderException("deleteUser");
        }
    }

    Container getContainer(UUID id) throws Exception {
        switch (providerType) {
            case SERVER:
                return serverProvider.get(id);
            case OFS:
                return ofsProvider.get(id);
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.get(id);
            default:
                throw createUnsupportedByProviderException("getContainer");
        }
    }

    ShellContainerInfo getContainerInfo(UUID id) throws InterruptedException, BrokerException, IllegalAccessException, IOException, SQLException {
        switch (providerType) {
            case SERVER:
                return new ShellContainerInfo(serverProvider.getInfo(id));
            case OFS:
                return new ShellContainerInfo(ofsProvider.getInfo(id));
            case SERVER_CACHE_OFS:
                return new ShellContainerInfo(serverCacheOfsProvider.getBrokerInfo(id));
            default:
                throw createUnsupportedByProviderException("getContainerInfo");
        }
    }

    EventPackage getEvents(EventActionType actionType, EventType eventType, Long startingId, Long endingId, UUID containerId, String containerType) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        switch (providerType) {
            case SERVER:
                return serverProvider.getEvents(actionType, eventType, startingId, endingId, containerId, containerType);
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.getEvents(actionType, eventType, startingId, endingId, containerId, containerType);
            default:
                throw createUnsupportedByProviderException("getEvents");
        }
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    IndexedECPublicKey getPublicKeyByIndex(UUID uuid, KeyType keyType, int index) throws Exception {
        switch (providerType) {
            case SERVER:
                return serverProvider.getPublicKeyByIndex(uuid, keyType, index);
            case OFS:
                return ofsProvider.getPublicKeyByIndex(uuid, keyType, index);
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.getPublicKeyByIndex(uuid, keyType, index);
            default:
                throw createUnsupportedByProviderException("getPublicKeyByIndex");
        }
    }

    IndexedECPublicKey getPublicKeyLatestActive(UUID uuid, KeyType keyType) throws Exception {
        switch (providerType) {
            case SERVER:
                return serverProvider.getPublicKeyLatestActive(uuid, keyType);
            case OFS:
                return ofsProvider.getPublicKeyLatestActive(uuid, keyType);
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.getPublicKeyLatestActive(uuid, keyType);
            default:
                throw createUnsupportedByProviderException("getPublicKeyLatestActive");
        }
    }

    List<IndexedECPublicKey> getPublicKeyList(UUID userUUID, KeyType keyType, Integer index) throws InterruptedException, BrokerException, SQLException, IllegalAccessException, IOException, InvalidKeySpecException {
        switch (providerType) {
            case SERVER:
                return serverProvider.getPublicKeyList(userUUID, keyType, index);
            case OFS:
                return ofsProvider.getPublicKeyList(userUUID, keyType, index);
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.getPublicKeyList(userUUID, keyType, index);
            default:
                throw createUnsupportedByProviderException("getPublicKeyList");
        }
    }

    String getReminder(UUID uuid) throws InterruptedException, IOException, BrokerException, IllegalAccessException {
        switch (providerType) {
            case SERVER:
                return serverProvider.getReminder(uuid);
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.getReminder(uuid);
            default:
                throw createUnsupportedByProviderException("getReminder");
        }
    }

    UUID getUserId() throws IllegalAccessException {
        switch (providerType) {
            case SERVER:
                return serverProvider.getUserId();
            case OFS:
                return ofsProvider.getUserId();
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.getUserId();
            default:
                throw createUnsupportedByProviderException("getUserId");
        }
    }

    void initializeOfsProvider(Path ofsRootDirectory, boolean partitionDataByUser) throws SQLException {
        if (ofsProvider.isAuthenticated()) {
            ofsProvider.logOut();
        }
        ofsProvider.initialize(ofsRootDirectory, partitionDataByUser);
        providerType = ProviderType.OFS;
    }

    void initializeServerCacheOfsProvider(String serverUrl, String apiKey, String applicationName, Path ofsRootDirectory, boolean partitionDataByUser) throws NoSuchAlgorithmException, SQLException, NoSuchPaddingException {
        if (serverCacheOfsProvider.isAuthenticated()) {
            serverCacheOfsProvider.logOut();
        }

        serverCacheOfsProvider.initialize(serverUrl, apiKey, applicationName, ofsRootDirectory, partitionDataByUser);
        providerType = ProviderType.SERVER_CACHE_OFS;
    }

    void initializeServerProvider(String serverUrl, String apiKey, String applicationName) throws NoSuchAlgorithmException, NoSuchPaddingException {
        if (serverProvider.isAuthenticated()) {
            serverProvider.logOut();
        }
        serverProvider.initialize(serverUrl, apiKey, applicationName);
        providerType = ProviderType.SERVER;
    }

    boolean isAuthenticated() throws IllegalAccessException {
        switch (providerType) {
            case SERVER:
                return serverProvider.isAuthenticated();
            case OFS:
                return ofsProvider.isAuthenticated();
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.isAuthenticated();
            default:
                throw createUnsupportedByProviderException("isAuthenticated");
        }
    }

    boolean isInitialized() throws IllegalAccessException {
        switch (providerType) {
            case SERVER:
                return serverProvider.isInitialized();
            case OFS:
                return ofsProvider.isInitialized();
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.isInitialized();
            default:
                throw createUnsupportedByProviderException("isInitialized");
        }
    }

    void logIn(UUID uuid, String password, String passphrase) throws BrokerException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, IllegalAccessException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, InterruptedException, SQLException, KeyFileNotFoundException {
        switch (providerType) {
            case SERVER:
                serverProvider.logIn(uuid, password, passphrase);
                break;
            case OFS:
                ofsProvider.logIn(uuid, password, passphrase);
                break;
            case SERVER_CACHE_OFS:
                serverCacheOfsProvider.logIn(uuid, password, passphrase);
                break;
            default:
                throw createUnsupportedByProviderException("logIn");
        }
    }

    void logOut() throws SQLException, IllegalAccessException {
        switch (providerType) {
            case SERVER:
                serverProvider.logOut();
                break;
            case OFS:
                ofsProvider.logOut();
                break;
            case SERVER_CACHE_OFS:
                serverCacheOfsProvider.logOut();
                break;
            default:
                throw createUnsupportedByProviderException("logOut");
        }
    }

    boolean needToSyncAccount(UUID uuid, String keyFilePath) throws InterruptedException, BrokerException, NoSuchAlgorithmException, IOException, IllegalAccessException {
        switch (providerType) {
            case SERVER:
                return serverProvider.needToSyncAccount(uuid, Files.readAllBytes(Paths.get(keyFilePath)));
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.needToSyncAccount(uuid);
            default:
                throw createUnsupportedByProviderException("needToSyncAccount");
        }
    }

    byte[] register(String password, String passphrase, String reminder) throws IllegalAccessException, NoSuchAlgorithmException, BrokerException, InvalidKeyException, SignatureException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IOException, IllegalBlockSizeException, InterruptedException, SQLException {
        switch (providerType) {
            case SERVER:
                return serverProvider.register(password, passphrase, reminder);
            case OFS:
                return ofsProvider.register(password, passphrase);
            case SERVER_CACHE_OFS:
                return serverCacheOfsProvider.register(password, passphrase, reminder);
            default:
                throw createUnsupportedByProviderException("register");
        }
    }

    void updateAccess(UUID id, List<Access> accesses) throws Exception {
        switch (providerType) {
            case SERVER:
                serverProvider.updateAccess(id, accesses);
                break;
            case OFS:
                ofsProvider.updateAccess(id, accesses);
                break;
            case SERVER_CACHE_OFS:
                serverCacheOfsProvider.updateAccess(id, accesses);
                break;
            default:
                throw createUnsupportedByProviderException("updateAccess");
        }
    }

    void updateContainer(Container container) throws Exception {
        switch (providerType) {
            case SERVER:
                serverProvider.createOrUpdate(container);
                break;
            case OFS:
                ofsProvider.createOrUpdate(container);
                break;
            case SERVER_CACHE_OFS:
                serverCacheOfsProvider.createOrUpdate(container);
                break;
            default:
                throw createUnsupportedByProviderException("updateContainer");
        }
    }

    void updateContent(UUID id, byte[] content) throws Exception {
        switch (providerType) {
            case SERVER:
                serverProvider.updateContent(id, content);
                break;
            case OFS:
                serverProvider.updateContent(id, content);
                break;
            case SERVER_CACHE_OFS:
                serverProvider.updateContent(id, content);
                break;
            default:
                throw createUnsupportedByProviderException("updateContent");
        }
    }

    void updateType(UUID id, String type) throws IllegalAccessException, InterruptedException, IOException, SQLException, ContainerNotFoundException, BrokerException {
        switch (providerType) {
            case SERVER:
                serverProvider.updateType(id, type);
                break;
            case OFS:
                ofsProvider.updateType(id, type);
                break;
            case SERVER_CACHE_OFS:
                serverCacheOfsProvider.updateType(id, type);
                break;
            default:
                throw createUnsupportedByProviderException("updateType");
        }
    }

    enum ProviderType {
        SERVER, OFS, SERVER_CACHE_OFS
    }

    public class ShellContainerInfo {
        private final List<Access> access;
        private final Metadata metadata;
        private String ofsLocation;
        private ZonedDateTime syncedAt;
        private String url;

        ShellContainerInfo(ContainerInfo info) {
            metadata = info.getMetadata();
            access = info.getAccess();
            url = info.getUrl();
        }

        ShellContainerInfo(ContainerDbInfo info) {
            metadata = info.getMetadata();
            access = info.getMetadata().getAccesses();
            ofsLocation = info.getOfsLocation();
            syncedAt = info.getSyncedAt();
        }

        public List<Access> getAccess() {
            return access;
        }

        Metadata getMetadata() {
            return metadata;
        }

        String getOfsLocation() {
            return ofsLocation;
        }

        ZonedDateTime getSyncedAt() {
            return syncedAt;
        }

        String getUrl() {
            return url;
        }
    }
}
