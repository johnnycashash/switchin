package com.switchin.event.api.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.switchin.event.api.model.Event;
import com.switchin.event.api.model.EventGetAllResponse;
import com.switchin.event.api.model.EventGetByIdResponse;
import com.switchin.event.api.repository.EventElasticRepository;
import com.switchin.event.utils.LogUtils;
import com.switchin.event.utils.QueryUtils;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class EventServiceElasticImpl extends EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceElasticImpl.class);

    private final EventElasticRepository eventElasticRepository;

    public EventServiceElasticImpl(EventElasticRepository eventElasticRepository) {
        this.eventElasticRepository = eventElasticRepository;
    }

    @Override
    public Future<EventGetAllResponse> readAll(String p, String l) {
        final int page = QueryUtils.getPage(p);
        final int limit = QueryUtils.getLimit(l);
        final int offset = QueryUtils.getOffset(page, limit);

        return eventElasticRepository.selectAll(limit, offset)
                .map(resp -> {
                    HitsMetadata<Event> eventHitsMetadata = resp.hits();
                    List<Hit<Event>> hits = eventHitsMetadata.hits();
                    final List<EventGetByIdResponse> events = hits.stream()
                            .map((Hit<Event> event) -> new EventGetByIdResponse(event.id(), event.source()))
                            .collect(Collectors.toList());

                    return new EventGetAllResponse(eventHitsMetadata.total().value(), limit, page, events);
                })
                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Read all events", success.getEvents())))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Read all events", throwable.getMessage())));
    }

    @Override
    public Future<EventGetByIdResponse> readOne(String id) {
        return eventElasticRepository.selectById(id)
                .map(resp -> new EventGetByIdResponse(resp.id(), resp.source()))
                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Read one event", success)))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Read one event", throwable.getMessage())));
    }


    @Override
    public Future<EventGetByIdResponse> create(Event event) {

        return eventElasticRepository.insert(event)
                .map(resp -> new EventGetByIdResponse(resp.id(), event))
                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Create one event", success)))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Create one event", throwable.getMessage())));
    }


    @Override
    public Future<EventGetByIdResponse> update(String id,
                                               Event event) {
        event.setId(id);


        return eventElasticRepository.update(event)
                .map(resp -> new EventGetByIdResponse(resp.id(), event))
                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Update one event", success)))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Update one event", throwable.getMessage())));
    }


    @Override
    public Future<Void> delete(String id) {

        return eventElasticRepository.delete(id)
                .onSuccess(success -> LOGGER.info(LogUtils.REGULAR_CALL_SUCCESS_MESSAGE.buildMessage("Delete one event", id)))
                .onFailure(throwable -> LOGGER.error(LogUtils.REGULAR_CALL_ERROR_MESSAGE.buildMessage("Delete one event", throwable.getMessage())));
    }
}
