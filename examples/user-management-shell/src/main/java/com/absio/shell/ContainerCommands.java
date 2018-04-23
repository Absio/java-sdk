package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.broker.mapper.ContainerInfo;
import com.absio.container.*;
import com.absio.provider.ServerProvider;
import com.absio.util.FileUtils;
import com.absio.util.StringUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;
import org.springframework.shell.table.*;
import org.threeten.bp.ZonedDateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("4. Container Commands")
public class ContainerCommands {
    @ShellMethodAvailability("*")
    public Availability containerMethodAvailabilityCheck() {
        return AbsioServerProvider.INSTANCE.isAuthenticated() ? Availability.available() : Availability.unavailable("The session must be authenticated.");
    }

    @ShellMethod("Encrypts a file to a SecuredContainer and uploads it to the Absio Broker server.")
    public String createContainer(String file, @ShellOption(defaultValue = ShellOption.NULL) String type, @ShellOption(defaultValue = ShellOption.NULL) String header) throws Exception {
        UUID id = UUID.randomUUID();
        Metadata metadata = new Metadata(id);
        metadata.setType(type);

        ServerProvider provider = AbsioServerProvider.INSTANCE;

        List<Access> accessList = new ArrayList<>();
        Access selfAccess = new Access(provider.getUserId(), Permission.DEFAULT_OWNER_PERMISSIONS);
        accessList.add(selfAccess);
        if (ShellUtils.promptForYesOrNo("Add additional recipients? Yes/No")) {
            promptForAccessList(accessList);
        }
        metadata.setAccesses(accessList);

        Container container = new Container(metadata, FileUtils.readBytesFromFile(file), header);

        EncryptionHelper helper = new EncryptionHelper(provider.getKeyRing(), provider.getPublicKeyMapper());
        SecuredContainer securedContainer = helper.encrypt(container);

        AbsioServerProvider.INSTANCE.createOrUpdate(securedContainer);
        return String.format("Container created successfully : %s", id.toString());
    }

    @ShellMethod("Deletes a SecuredContainer from the Absio Broker server.")
    public String deleteContainer(UUID id) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        AbsioServerProvider.INSTANCE.delete(id);
        return String.format("Container deleted successfully : %s", id);
    }

    @ShellMethod("Gets a SecuredContainer from the Absio Broker server and decrypts it to a file.")
    public Table getContainer(UUID id, String filePath) throws Exception {
        SecuredContainer securedContainer = AbsioServerProvider.INSTANCE.get(id);

        EncryptionHelper helper = new EncryptionHelper(AbsioServerProvider.INSTANCE.getKeyRing(), AbsioServerProvider.INSTANCE.getPublicKeyMapper());
        Container container = helper.decrypt(securedContainer);

        FileUtils.writeToFile(filePath, container.getContent());

        System.out.println("Container : " + id);
        System.out.println("CustomData : " + container.getCustomData());
        return printMetadata(container.getMetadata());
    }

    @ShellMethod("Gets the SecuredContainer information from the Absio Broker server.")
    public Table getContainerInfo(UUID id) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        ContainerInfo info = AbsioServerProvider.INSTANCE.getInfo(id);
        System.out.println("Container : " + id);
        return printMetadata(info.getMetadata());
    }

    private Table printAccesses(List<Access> accesses) {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("userId", "User ID");
        headers.put("createdAt", "Created At");
        headers.put("createdBy", "Created By");
        headers.put("expiresAt", "Expires At");
        headers.put("modifiedAt", "Modified At");
        headers.put("modifiedBy", "Modified By");
        headers.put("permissions", "Permissions");
        TableModel model = new BeanListTableModel<>(accesses, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        return tableBuilder.addFullBorder(BorderStyle.oldschool).build();
    }

    private Table printMetadata(Metadata metadata) {
        System.out.println("Created by : " + metadata.getCreatedBy());
        System.out.println("Created at : " + metadata.getCreatedAt());
        System.out.println("Type : " + metadata.getType());
        System.out.println("Length : " + metadata.getLength());
        if (metadata.getModifiedBy() != null) {
            System.out.println("Modified by : " + metadata.getModifiedBy());
        }
        if (metadata.getModifiedAt() != null) {
            System.out.println("Modified at : " + metadata.getModifiedAt());
        }
        System.out.println("Accesses :");
        return printAccesses(metadata.getAccesses());
    }

    private void promptForAccessList(List<Access> accessList) {
        while (true) {
            String userIdResponse = ShellUtils.promptForString("User ID? (Leave blank if finished)");
            if (StringUtils.isEmpty(userIdResponse)) {
                break;
            }
            UUID userId = UUID.fromString(userIdResponse);

            String permissionResponse = ShellUtils.promptForString("Permission? (Default: com.absio.container.Permission.DEFAULT_USER_PERMISSIONS)");
            int permission;
            if (!StringUtils.isEmpty(permissionResponse)) {
                permission = Permission.valueOf(permissionResponse).getFlag();
            }
            else {
                permission = Permission.DEFAULT_USER_PERMISSIONS;
            }

            ZonedDateTime expiresAt = ZonedDateTime.parse(ShellUtils.promptForString("Expiration date? (Default: No expiration)"));

            Access recipientAccess = new Access(userId, permission, expiresAt);
            accessList.add(recipientAccess);
        }
    }

    @ShellMethod("Updates a SecuredContainer on the Absio Broker server with new type, header, content, or access.")
    public String updateContainer(UUID id, @ShellOption(defaultValue = ShellOption.NONE) String file, @ShellOption(defaultValue = ShellOption.NONE) String type, @ShellOption(defaultValue = ShellOption.NONE) String header) throws Exception {
        ServerProvider provider = AbsioServerProvider.INSTANCE;

        SecuredContainer serverContainer = provider.get(id);
        EncryptionHelper helper = new EncryptionHelper(provider.getKeyRing(), provider.getPublicKeyMapper());

        Container container = helper.decrypt(serverContainer);

        if (!type.equals(ShellOption.NONE)) {
            container.getMetadata().setType(type);
        }

        if (!file.equals(ShellOption.NONE)) {
            container.setContent(FileUtils.readBytesFromFile(file));
        }

        if (!header.equals(ShellOption.NONE)) {
            container.setCustomData(header);
        }

        if (ShellUtils.promptForYesOrNo("Change recipients? Yes/No")) {
            List<Access> accessList = new ArrayList<>();
            Access selfAccess = new Access(provider.getUserId(), Permission.DEFAULT_OWNER_PERMISSIONS);
            accessList.add(selfAccess);
            promptForAccessList(accessList);
            container.getMetadata().setAccesses(accessList);
        }

        SecuredContainer securedContainer = helper.encrypt(container);

        AbsioServerProvider.INSTANCE.createOrUpdate(securedContainer);
        return String.format("Container updated successfully : %s", id.toString());
    }
}
