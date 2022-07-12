package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.broker.mapper.AbstractEvent;
import com.absio.broker.mapper.EventActionType;
import com.absio.broker.json.EventPackage;
import com.absio.broker.mapper.EventType;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;
import org.springframework.shell.table.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("5. Event Commands")
public class EventCommands {
    @ShellMethodAvailability("*")
    public Availability eventMethodAvailabilityCheck() throws IllegalAccessException {
        return AbsioProvider.INSTANCE.isAuthenticated() ? Availability.available() : Availability.unavailable("The session must be authenticated.");
    }

    @ShellMethod("Gets all events that match the event type, action type, starting ID, ending ID, container ID, and type.")
    public String getEvents(@ShellOption(defaultValue = ShellOption.NULL, help = "{ACCESSED | ADDED | DELETED | UPDATED}") EventActionType actionType, @ShellOption(defaultValue = ShellOption.NULL, help = "{CONTAINER | KEYS_FILE}") EventType eventType, @ShellOption(defaultValue = ShellOption.NULL) Long startingId, @ShellOption(defaultValue = ShellOption.NULL) Long endingId, @ShellOption(defaultValue = ShellOption.NULL) UUID containerId, @ShellOption(defaultValue = ShellOption.NULL) String containerType) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        EventPackage events = AbsioProvider.INSTANCE.getEvents(actionType, eventType, startingId, endingId, containerId, containerType);
        return printEvents(events);
    }

    private Table printContainerEvents(List<AbstractEvent> eventList) {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Event ID");
        headers.put("date", "Date");
        headers.put("eventType", "Event Type");
        headers.put("clientAppName", "Client App Name");
        headers.put("containerId", "Container ID");
        headers.put("userId", "User ID");
        headers.put("type", "Type");
        headers.put("expiredAt", "Expired At");
        headers.put("modifiedAt", "Modified At");
        headers.put("changesJson", "Changes");
        TableModel model = new BeanListTableModel<>(eventList, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        return tableBuilder.addFullBorder(BorderStyle.oldschool).build();
    }

    private String printEvents(EventPackage events) {
        List<AbstractEvent> containerEvents = new ArrayList<>();
        List<AbstractEvent> keyFileEvents = new ArrayList<>();
        for (AbstractEvent event : events.getEvents()) {
            switch (event.getEventType()) {
                case CONTAINER:
                    containerEvents.add(event);
                    break;
                case KEYS_FILE:
                    keyFileEvents.add(event);
                    break;
            }
        }

        Table one = printContainerEvents(containerEvents);
        Table two = printKeyFileEvents(keyFileEvents);

        return "\nContainer Events\n" + one.render(80) + "\n\nKey File Events\n" + two.render(80);
    }

    private Table printKeyFileEvents(List<AbstractEvent> eventList) {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Event ID");
        headers.put("date", "Date");
        headers.put("eventType", "Event Type");
        headers.put("actionType", "Action Type");
        headers.put("changesJson", "Changes");
        TableModel model = new BeanListTableModel<>(eventList, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        return tableBuilder.addFullBorder(BorderStyle.oldschool).build();
    }
}
