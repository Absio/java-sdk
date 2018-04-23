package com.absio.shell;

import com.absio.broker.BrokerException;
import com.absio.broker.mapper.AbstractEvent;
import com.absio.broker.mapper.EventActionType;
import com.absio.broker.mapper.EventPackage;
import com.absio.broker.mapper.EventType;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;
import org.springframework.shell.table.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("5. Event Commands")
public class EventCommands {
    @ShellMethodAvailability("*")
    public Availability eventMethodAvailabilityCheck() {
        return AbsioServerProvider.INSTANCE.isAuthenticated() ? Availability.available() : Availability.unavailable("The session must be authenticated.");
    }

    @ShellMethod("Gets all events that match the event type, action type, starting ID, ending ID, container ID, and type.")
    public Table getEvents(@ShellOption(defaultValue = ShellOption.NULL) EventActionType actionType, @ShellOption(defaultValue = ShellOption.NULL) EventType eventType, @ShellOption(defaultValue = ShellOption.NULL) Long startingId, @ShellOption(defaultValue = ShellOption.NULL) Long endingId, @ShellOption(defaultValue = ShellOption.NULL) UUID containerId, @ShellOption(defaultValue = ShellOption.NULL) String containerType) throws InterruptedException, BrokerException, IllegalAccessException, IOException {
        EventPackage events = AbsioServerProvider.INSTANCE.getEvents(actionType, eventType, startingId, endingId, containerId, containerType);
        return printEvents(events);
    }

    private Table printEvents(EventPackage events) {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        List<AbstractEvent> eventList = events.getEvents();
        headers.put("id", "Event ID");
        headers.put("date", "Date");
        headers.put("eventType", "Event Type");
        headers.put("actionType", "Action Type");
        TableModel model = new BeanListTableModel<>(eventList, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        return tableBuilder.addFullBorder(BorderStyle.oldschool).build();
    }
}
