package com.switchin.event.api.router;

import com.switchin.event.api.handler.EventHandler;
import com.switchin.event.api.handler.EventValidationHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;

public class EventRouter {

    private final Vertx vertx;
    private final EventHandler eventHandler;
    private final EventValidationHandler eventValidationHandler;

    public EventRouter(Vertx vertx,
                       EventHandler eventHandler,
                       EventValidationHandler eventValidationHandler) {
        this.vertx = vertx;
        this.eventHandler = eventHandler;
        this.eventValidationHandler = eventValidationHandler;
    }


    public void setRouter(Router router) {
        router.mountSubRouter("/api/v1", buildEventRouter());
    }


    private Router buildEventRouter() {
        final Router eventRouter = Router.router(vertx);

        eventRouter.route("/events*").handler(BodyHandler.create());
        eventRouter.get("/events").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(eventValidationHandler.readAll()).handler(eventHandler::readAll);
        eventRouter.get("/events/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(eventValidationHandler.readOne()).handler(eventHandler::readOne);
        eventRouter.post("/events").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(eventValidationHandler.create()).handler(eventHandler::create);
        eventRouter.put("/events/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(eventValidationHandler.update()).handler(eventHandler::update);
        eventRouter.delete("/events/:id").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(eventValidationHandler.delete()).handler(eventHandler::delete);

        return eventRouter;
    }

}
