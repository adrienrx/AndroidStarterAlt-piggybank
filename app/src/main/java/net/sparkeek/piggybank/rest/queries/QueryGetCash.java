package net.sparkeek.piggybank.rest.queries;

import net.sparkeek.piggybank.ApplicationAndroidStarter;
import net.sparkeek.piggybank.bus.event.AbstractEventQueryDidFinish;
import net.sparkeek.piggybank.persistence.entities.CashEntity;
import net.sparkeek.piggybank.rest.CashService;
import net.sparkeek.piggybank.rest.dto.DTOCash;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Adrien on 18/11/2016.
 */
@AutoInjector(ApplicationAndroidStarter.class)
public class QueryGetCash extends AbstractQuery {

    //region Injected fields dagger
    @Inject
    transient CashService cashService;
    @Inject
    transient EventBus eventBus;
    @Inject
    transient SingleEntityStore<Persistable> dataStore;
    //endregion

    //region Fields
    public final boolean pullToRefresh;
    transient public List<DTOCash> results;
    //endregion

    //region Constructor matching super
    protected QueryGetCash(final boolean pbPullToRefresh){
        super(Priority.MEDIUM);
        pullToRefresh = pbPullToRefresh;
    }
    //endregion


    @Override
    public void inject() {
        ApplicationAndroidStarter.sharedApplication().componentApplication().inject(this);
    }

    @Override
    protected void execute() throws Exception {
        inject();

        final Call<List<DTOCash>> loCall = cashService.listCash();
        final Response<List<DTOCash>> loExecute = loCall.execute();
        results = loExecute.body();

        final ArrayList<CashEntity> lloEntities = new ArrayList<>();
        for(final DTOCash loDtoCash : results){
            final CashEntity loCash = new CashEntity();
            loCash.setCash(loDtoCash.cash);
        }
        dataStore.insert(lloEntities).toBlocking().value();
    }

    @Override
    protected void postEventQueryFinished() {
        final EventQueryGetCashDidFinish loEvent = new EventQueryGetCashDidFinish(this, mSuccess, mErrorType, mThrowable, false ,results);
        eventBus.post(loEvent);
    }

    @Override
    public void postEventQueryFinishedNoNetwork() {
        final EventQueryGetCashDidFinish loEvent = new EventQueryGetCashDidFinish(this, false, AbstractEventQueryDidFinish.ErrorType.NETWORK_UNREACHABLE, null, false, null);
        eventBus.post(loEvent);
    }

    //region EventQueryDidFinish
    public static final class EventQueryGetCashDidFinish extends AbstractEventQueryDidFinish<QueryGetCash>{
        public final List<DTOCash> results;
        public final boolean pullToRefresh;

        public EventQueryGetCashDidFinish(final QueryGetCash poQuery, final boolean pbSuccess, final ErrorType poErrorType, final Throwable poThrowable, final boolean pbPullToRefresh, final List<DTOCash> ploResults) {
            super(poQuery, pbSuccess, poErrorType, poThrowable);
            pullToRefresh = pbPullToRefresh;
            results = ploResults;
        }
    }
}
