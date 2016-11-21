package net.sparkeek.piggybank.bus.event;

import net.sparkeek.piggybank.rest.queries.AbstractQuery;

public abstract class AbstractEventQueryDidFinish<QueryType extends AbstractQuery> extends AbstractEvent {
    public enum ErrorType {
        UNKNOWN,
        NETWORK_UNREACHABLE
    }

    public final QueryType query;

    public final boolean success;
    public final ErrorType errorType;
    public final Throwable throwable;

    public AbstractEventQueryDidFinish(final QueryType poQuery, final boolean pbSuccess, final ErrorType poErrorType, final Throwable poThrowable) {
        query = poQuery;
        success = pbSuccess;
        errorType = poErrorType;
        throwable = poThrowable;
    }
}
