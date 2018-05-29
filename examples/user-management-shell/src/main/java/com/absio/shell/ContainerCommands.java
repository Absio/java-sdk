package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.container.Access;
import com.absio.container.Container;
import com.absio.container.Metadata;
import com.absio.container.Permission;
import com.absio.util.GeneralUtils;
import com.absio.util.StringUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;
import org.springframework.shell.table.*;
import org.threeten.bp.ZonedDateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("4. Container Commands")
public class ContainerCommands {
    private static final String FILE_TYPE = "File";

    private boolean accessListContainsUser(List<Access> accessList, UUID userId) {
        if (accessList != null) {
            for (Access access : accessList) {
                if (GeneralUtils.equals(userId, access.getUserId())) {
                    return true;
                }
            }
        }

        return false;
    }

    @ShellMethodAvailability("*")
    public Availability containerMethodAvailabilityCheck() throws IllegalAccessException {
        return AbsioProvider.INSTANCE.isAuthenticated() ? Availability.available() : Availability.unavailable("The session must be authenticated.");
    }

    @ShellMethod("Encrypts a file to a SecuredContainer and uploads it to the Absio Broker server.")
    public String createContainer(String file, @ShellOption(defaultValue = FILE_TYPE) String type, @ShellOption(defaultValue = ShellOption.NULL) String header, @ShellOption(defaultValue = ShellOption.NULL) UUID id) throws Exception {
        if (id == null) {
            id = UUID.randomUUID();
        }
        Metadata metadata = new Metadata(id);
        metadata.setType(type);
        if (header == null && FILE_TYPE.equals(type)) {
            File theFile = new File(file);
            header = theFile.getName();
        }

        List<Access> accessList = new ArrayList<>();
        Access selfAccess = new Access(AbsioProvider.INSTANCE.getUserId(), Permission.DEFAULT_OWNER_PERMISSIONS);
        accessList.add(selfAccess);
        if (ShellUtils.promptForYesOrNo("Add additional recipients? Yes/No")) {
            promptForAccessList(accessList);
        }
        metadata.setAccesses(accessList);

        Container container = new Container(metadata, Files.readAllBytes(Paths.get(file)), header);

        AbsioProvider.INSTANCE.createOrUpdateContainer(container);
        return String.format("Container created successfully : %s", id.toString());
    }

    @ShellMethod("Deletes a SecuredContainer from the Absio Broker server.")
    public String deleteContainer(UUID id) throws InterruptedException, BrokerException, IllegalAccessException, IOException, SQLException {
        AbsioProvider.INSTANCE.deleteContainer(id);
        return String.format("Container deleted successfully : %s", id);
    }

    @ShellMethod("Gets a SecuredContainer from the Absio Broker server and decrypts it to a file.")
    public Table getContainer(UUID id, String filePath) throws Exception {
        Container container = AbsioProvider.INSTANCE.getContainer(id);

        Files.write(Paths.get(filePath), container.getContent());

        System.out.println("Container : " + id);
        System.out.println("CustomData : " + container.getCustomData());
        return printMetadata(container.getMetadata());
    }

    @ShellMethod("Gets the SecuredContainer information from the Absio Broker server.")
    public Table getContainerInfo(UUID id) throws InterruptedException, BrokerException, IllegalAccessException, IOException, SQLException {
        AbsioProvider.ShellContainerInfo info = AbsioProvider.INSTANCE.getContainerInfo(id);
        System.out.println("Container : " + id);
        return printContainerInfo(info);
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

    private Table printContainerInfo(AbsioProvider.ShellContainerInfo containerInfo) {
        if (containerInfo.getOfsLocation() != null) {
            System.out.println("OFS Location : " + containerInfo.getOfsLocation());
        }
        if (containerInfo.getSyncedAt() != null) {
            System.out.println("Synced at : " + containerInfo.getSyncedAt());
        }
        if (containerInfo.getUrl() != null) {
            System.out.println("Storage URL : " + containerInfo.getUrl());
        }
        Metadata metadata = containerInfo.getMetadata();
        return printMetadata(metadata);
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

            String permissionResponse = ShellUtils.promptForString("Permission? (Default: com.absio.container.Permission.DEFAULT_USER_PERMISSIONS - 75)");
            int permission;
            if (!StringUtils.isEmpty(permissionResponse)) {
                permission = Integer.parseInt(permissionResponse);
            }
            else {
                permission = Permission.DEFAULT_USER_PERMISSIONS;
            }

            String expirationResponse = ShellUtils.promptForString("Expiration date? (Default: No expiration)");
            ZonedDateTime expiresAt = null;
            if (!StringUtils.isEmpty(expirationResponse)) {
                expiresAt = ZonedDateTime.parse(expirationResponse);
            }

            Access recipientAccess = new Access(userId, permission, expiresAt);
            accessList.add(recipientAccess);
        }
    }

    @ShellMethod("Updates a SecuredContainer on the Absio Broker server with new type, header, content, or access.")
    public String updateContainer(UUID id, @ShellOption(defaultValue = ShellOption.NULL) String file, @ShellOption(defaultValue = ShellOption.NULL) String type, @ShellOption(defaultValue = ShellOption.NULL) String header) throws Exception {
        Container container = AbsioProvider.INSTANCE.getContainer(id);
        boolean typeChanged = false;

        if (file != null) {
            if (type == null) {
                type = FILE_TYPE;
            }
            if (header == null) {
                File theFile = new File(file);
                header = theFile.getName();
            }
        }

        Metadata metadata = container.getMetadata();
        if (type != null && !type.equals(ShellOption.NONE) && !GeneralUtils.equals(metadata.getType(), type)) {
            typeChanged = true;
            metadata.setType(type);
        }

        if (file != null && !file.equals(ShellOption.NONE)) {
            container.setContent(Files.readAllBytes(Paths.get(file)));
        }

        if (header != null && !header.equals(ShellOption.NONE)) {
            container.setCustomData(header);
        }

        boolean changeAccess = ShellUtils.promptForYesOrNo("Change recipients? Yes/No");
        if (changeAccess) {
            List<Access> accessList = new ArrayList<>();
            promptForAccessList(accessList);
            if (!accessListContainsUser(accessList, AbsioProvider.INSTANCE.getUserId())) {
                Access selfAccess = new Access(AbsioProvider.INSTANCE.getUserId(), Permission.DEFAULT_OWNER_PERMISSIONS);
                accessList.add(selfAccess);
            }
            metadata.setAccesses(accessList);
        }

        if (file != null && changeAccess) {
            AbsioProvider.INSTANCE.updateContainer(container);
        }
        else {
            if (typeChanged) {
                AbsioProvider.INSTANCE.updateType(id, type);
            }
            if (changeAccess) {
                AbsioProvider.INSTANCE.updateAccess(id, metadata.getAccesses());
            }
            if (file != null) {
                AbsioProvider.INSTANCE.updateContent(id, container.getContent());
            }
        }
        return String.format("Container updated successfully : %s", id.toString());
    }
}
