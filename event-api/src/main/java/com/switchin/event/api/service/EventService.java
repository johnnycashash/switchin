package com.switchin.event.api.service;

import com.switchin.event.api.model.Event;
import com.switchin.event.api.model.EventGetAllResponse;
import com.switchin.event.api.model.EventGetByIdResponse;
import io.vertx.core.Future;

public abstract class EventService {

    public abstract Future<EventGetAllResponse> readAll(String page, String limit);

    public abstract Future<EventGetByIdResponse> readOne(String id);

    public abstract Future<EventGetByIdResponse> create(Event event);

    public abstract Future<EventGetByIdResponse> update(String id,
                                                        Event event);

    public abstract Future<Void> delete(String id);
}
