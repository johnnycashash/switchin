package com.switchin.auth;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.KeycloakHelper;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.OAuth2AuthHandler;

public class MainVerticle extends AbstractVerticle {
    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route("/").handler(req -> req.response().end("Hello Vert.x!"));
        JsonObject keycloakJson = new JsonObject()
                .put("realm", "switchin")
                .put("auth-server-url", "http://localhost:9080/auth")
                .put("ssl-required", "external")
                .put("resource", "event")
                .put("public-client", true)
                .put("confidential-port", 0);

        OAuth2Auth oauth2 = KeycloakAuth.create(vertx, OAuth2FlowType.AUTH_CODE, keycloakJson);
        OAuth2AuthHandler oauth2Handler = OAuth2AuthHandler.create(vertx, oauth2);
        oauth2Handler.setupCallback(router.get("/callback"));

        router.route("/protected/*").handler(oauth2Handler);
        router.get("/protected/currentuser").handler(this::currentUser);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }

    public void currentUser(RoutingContext context) {
        String accessToken = context.user().principal().getString("access_token");
        JsonObject token = KeycloakHelper.parseToken(accessToken);
        context.response().end("User " + token.getString("preferred_username"));
    }

}
