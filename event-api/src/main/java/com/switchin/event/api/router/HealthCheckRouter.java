package com.switchin.event.api.router;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;

public class HealthCheckRouter {

    private HealthCheckRouter() {

    }

    public static void setRouter(Vertx vertx,
                                 Router router,
                                 ElasticsearchAsyncClient elasticsearchAsyncClient) {
        final HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);

        healthCheckHandler.register("elasticsearch",
                promise ->
                        elasticsearchAsyncClient.ping().whenComplete((booleanResponse, throwable) -> {
                            if (throwable != null) {
                                promise.fail(throwable.getCause());
                            } else if (!booleanResponse.value()) {
                                promise.fail("Elastic Health failed ping");
                            } else {
                                promise.complete(Status.OK());
                            }
                        })
        );

        router.get("/health").handler(healthCheckHandler);
    }

}
