package com.switchin.event.verticle;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import com.switchin.event.api.handler.ErrorHandler;
import com.switchin.event.api.handler.EventHandler;
import com.switchin.event.api.handler.EventValidationHandler;
import com.switchin.event.api.repository.EventElasticRepository;
import com.switchin.event.api.router.EventRouter;
import com.switchin.event.api.router.HealthCheckRouter;
import com.switchin.event.api.router.MetricsRouter;
import com.switchin.event.api.service.EventService;
import com.switchin.event.api.service.EventServiceElasticImpl;
import com.switchin.event.utils.DbUtils;
import com.switchin.event.utils.LogUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class ApiVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiVerticle.class);

    @Override
    public void start(Promise<Void> promise) {
        //Elastic DB
        final ElasticsearchAsyncClient elasticsearchClient = DbUtils.buildElasticClient();
        final EventElasticRepository eventElasticRepository = new EventElasticRepository(elasticsearchClient);
        final EventService eventElasticService = new EventServiceElasticImpl(eventElasticRepository);

        final EventHandler eventHandler = new EventHandler(eventElasticService);
        final EventValidationHandler eventValidationHandler = new EventValidationHandler(vertx);
        final EventRouter eventRouter = new EventRouter(vertx, eventHandler, eventValidationHandler);

        final Router router = Router.router(vertx);
        ErrorHandler.buildHandler(router);
        HealthCheckRouter.setRouter(vertx, router, elasticsearchClient);
        MetricsRouter.setRouter(router);
        eventRouter.setRouter(router);

        buildHttpServer(vertx, promise, router);
    }


    private void buildHttpServer(Vertx vertx,
                                 Promise<Void> promise,
                                 Router router) {
        final int port = 8888;

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(port, http -> {
                    if (http.succeeded()) {
                        promise.complete();
                        LOGGER.info(LogUtils.RUN_HTTP_SERVER_SUCCESS_MESSAGE.buildMessage(port));
                    } else {
                        promise.fail(http.cause());
                        LOGGER.info(LogUtils.RUN_HTTP_SERVER_ERROR_MESSAGE.buildMessage());
                    }
                });
    }

}
