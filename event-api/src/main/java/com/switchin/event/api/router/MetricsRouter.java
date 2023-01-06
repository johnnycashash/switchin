package com.switchin.event.api.router;

import io.vertx.ext.web.Router;
import io.vertx.micrometer.PrometheusScrapingHandler;

public class MetricsRouter {

    private MetricsRouter() {

    }


    public static void setRouter(Router router) {
        router.route("/metrics").handler(PrometheusScrapingHandler.create());
    }

}
