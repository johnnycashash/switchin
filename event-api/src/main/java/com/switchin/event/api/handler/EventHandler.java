package com.switchin.event.api.handler;

import com.switchin.event.api.model.Event;
import com.switchin.event.api.model.EventGetAllResponse;
import com.switchin.event.api.model.EventGetByIdResponse;
import com.switchin.event.api.service.EventService;
import com.switchin.event.utils.ResponseUtils;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

public class EventHandler {

    private static final String ID_PARAMETER = "id";
    private static final String PAGE_PARAMETER = "page";
    private static final String LIMIT_PARAMETER = "limit";

    private final EventService eventService;

    public EventHandler(EventService eventService) {
        this.eventService = eventService;
    }


    public Future<EventGetAllResponse> readAll(RoutingContext rc) {
        final String page = rc.queryParams().get(PAGE_PARAMETER);
        final String limit = rc.queryParams().get(LIMIT_PARAMETER);

        return eventService.readAll(page, limit)
                .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
                .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
    }


    public Future<EventGetByIdResponse> readOne(RoutingContext rc) {
        final String id = rc.pathParam(ID_PARAMETER);

        return eventService.readOne(id)
                .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
                .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
    }


    public Future<EventGetByIdResponse> create(RoutingContext rc) {
        final Event event = rc.getBodyAsJson().mapTo(Event.class);

        return eventService.create(event)
                .onSuccess(success -> ResponseUtils.buildCreatedResponse(rc, success))
                .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
    }


    public Future<EventGetByIdResponse> update(RoutingContext rc) {
        final String id = rc.pathParam(ID_PARAMETER);
        final Event event = rc.getBodyAsJson().mapTo(Event.class);

        return eventService.update(id, event)
                .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
                .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
    }


    public Future<Void> delete(RoutingContext rc) {
        final String id = rc.pathParam(ID_PARAMETER);

        return eventService.delete(id)
                .onSuccess(success -> ResponseUtils.buildNoContentResponse(rc))
                .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
    }

}
