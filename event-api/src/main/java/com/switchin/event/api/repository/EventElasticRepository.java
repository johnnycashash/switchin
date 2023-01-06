package com.switchin.event.api.repository;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.*;
import com.switchin.event.api.exception.EventDeleteElasticException;
import com.switchin.event.api.exception.EventElasticException;
import com.switchin.event.api.model.Event;
import com.switchin.event.utils.LogUtils;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class EventElasticRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventElasticRepository.class);

    private final ElasticsearchAsyncClient elasticsearchAsyncClient;


    public EventElasticRepository(ElasticsearchAsyncClient elasticsearchAsyncClient) {
        this.elasticsearchAsyncClient = elasticsearchAsyncClient;
    }

    public Future<SearchResponse<Event>> selectAll(
            int limit,
            int offset) {
        return Future.fromCompletionStage(elasticsearchAsyncClient.search(new SearchRequest.Builder().index("events").from(offset).size(limit).build(), Event.class)
                        .handle((eventSearchResponse, throwable) -> {
                            if (throwable != null) {
                                throw new EventElasticException(throwable);
                            }
                            return eventSearchResponse;
                        }), Vertx.currentContext()).onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Read all events", success.hits().total().value())))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Read all events", throwable.getMessage())));
    }

    public Future<GetResponse<Event>> selectById(
            String id) {
        return Future.fromCompletionStage(elasticsearchAsyncClient.get(new GetRequest.Builder().index("events").id(id).build(), Event.class)
                        .handle((eventSearchResponse, throwable) -> {
                            if (throwable != null) {
                                throw new EventElasticException(throwable);
                            }
                            if (eventSearchResponse.found()) {
                                return eventSearchResponse;
                            } else {
                                throw new EventDeleteElasticException("Unable to Find : " + eventSearchResponse.id());
                            }
                        }), Vertx.currentContext()).onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Read event by id", success.id())))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Read event by id", throwable.getMessage())));
    }

    public Future<IndexResponse> insert(
            Event event) {
        return Future.fromCompletionStage(elasticsearchAsyncClient.index(new IndexRequest.Builder<>().index("events").document(event).build())
                        .handle((eventSearchResponse, throwable) -> {
                            if (throwable != null) {
                                throw new EventElasticException(throwable);
                            }
                            if (eventSearchResponse.result().compareTo(Result.Created) == 0) {
                                return eventSearchResponse;
                            } else {
                                throw new EventDeleteElasticException("Unable to Insert : " + eventSearchResponse.result().name());
                            }
                        }), Vertx.currentContext()).onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Insert event", success.id())))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Insert event", throwable.getMessage())));
    }

    public Future<UpdateResponse<Event>> update(
            Event event) {
        return Future.fromCompletionStage(elasticsearchAsyncClient.update(new UpdateRequest.Builder<Event, Event>().index("events").id(event.getId()).doc(event).build(), Event.class)
                        .handle((eventSearchResponse, throwable) -> {
                            if (throwable != null) {
                                throw new EventElasticException(throwable);
                            }
                            if (eventSearchResponse.result().compareTo(Result.Updated) == 0) {
                                return eventSearchResponse;
                            } else {
                                throw new EventDeleteElasticException("Unable to Update : " + eventSearchResponse.result().name());
                            }
                        }), Vertx.currentContext()).onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Update event", success.id())))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Update event", throwable.getMessage())));
    }

    public Future<Void> delete(
            String id) {
        return Future.fromCompletionStage(elasticsearchAsyncClient.delete(new DeleteRequest.Builder().index("events").id(id).build())
                        .handle((eventSearchResponse, throwable) -> {
                            if (throwable != null) {
                                throw new EventElasticException(throwable);
                            }
                            if (eventSearchResponse.result().compareTo(Result.Deleted) == 0) {
                                return (Void) null;
                            } else {
                                throw new EventDeleteElasticException("Unable to Delete : " + eventSearchResponse.result().name());
                            }
                        }), Vertx.currentContext()).onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Delete event", id)))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Delete event", throwable.getMessage())));
    }
}
