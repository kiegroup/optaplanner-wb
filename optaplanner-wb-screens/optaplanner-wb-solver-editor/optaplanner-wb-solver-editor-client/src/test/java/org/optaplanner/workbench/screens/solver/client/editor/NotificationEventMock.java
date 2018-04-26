package org.optaplanner.workbench.screens.solver.client.editor;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Event;

import org.uberfire.workbench.events.NotificationEvent;

public class NotificationEventMock
        implements Event<NotificationEvent> {

    @Override
    public void fire(NotificationEvent notificationEvent) {

    }

    @Override
    public Event<NotificationEvent> select(Annotation... annotations) {
        return null;
    }

    @Override
    public <U extends NotificationEvent> Event<U> select(Class<U> aClass,
                                                         Annotation... annotations) {
        return null;
    }
}
