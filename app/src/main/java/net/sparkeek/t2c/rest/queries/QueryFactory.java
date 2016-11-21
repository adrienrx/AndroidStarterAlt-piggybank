package net.sparkeek.t2c.rest.queries;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import net.sparkeek.t2c.services.ServiceQueryExecutorIntentBuilder;

@Singleton
public class QueryFactory {
    //region Build methods
    public QueryGetRepos buildQueryGetRepos(@NonNull final String psUser, final boolean pbPullToRefresh) {
        return new QueryGetRepos(psUser, pbPullToRefresh);
    }

    public QueryGetCash buildQueryGetCash(final boolean pbPullToRefresh){
        return new QueryGetCash(pbPullToRefresh);
    }
    //endregion

    //region Start methods
    public void startQuery(@NonNull final Context poContext, @NonNull final AbstractQuery poQuery) {
        final Intent loIntent = new ServiceQueryExecutorIntentBuilder(poQuery).build(poContext);
        poContext.startService(loIntent);
    }

    public void startQueryGetRepos(@NonNull final Context poContext, @NonNull final String psUser, final boolean pbPullToRefresh) {
        final QueryGetRepos loQuery = buildQueryGetRepos(psUser, pbPullToRefresh);
        startQuery(poContext, loQuery);
    }
    //endregion

    public void startQueryGetCash(@NonNull final Context poContext, final boolean pbPullToRefresh){
        final QueryGetCash loQuery = buildQueryGetCash(pbPullToRefresh);
        startQuery(poContext, loQuery);
    }
}
