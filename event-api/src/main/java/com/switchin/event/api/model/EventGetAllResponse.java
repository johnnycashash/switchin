package com.switchin.event.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class EventGetAllResponse implements Serializable {

    private static final long serialVersionUID = -8964658883487451260L;

    @JsonProperty(value = "total")
    private final long total;

    @JsonProperty(value = "limit")
    private final int limit;

    @JsonProperty(value = "page")
    private final int page;

    @JsonProperty(value = "events")
    private final List<EventGetByIdResponse> events;

    public EventGetAllResponse(long total,
                               int limit,
                               int page,
                               List<EventGetByIdResponse> events) {
        this.total = total;
        this.limit = limit;
        this.page = page;
        this.events = events;
    }

    public long getTotal() {
        return total;
    }

    public int getLimit() {
        return limit;
    }

    public int getPage() {
        return page;
    }

    public List<EventGetByIdResponse> getEvents() {
        return events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventGetAllResponse that = (EventGetAllResponse) o;
        return total == that.total &&
                limit == that.limit &&
                page == that.page &&
                events.equals(that.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, limit, page, events);
    }

    @Override
    public String toString() {
        return "EventGetAllResponse{" +
                "total=" + total +
                ", limit=" + limit +
                ", page=" + page +
                ", events=" + events +
                '}';
    }

}
