package net.sparkeek.t2c.mvp.cashDisplay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import net.sparkeek.t2c.ApplicationAndroidStarter;
import net.sparkeek.t2c.persistence.entities.Cash;
import net.sparkeek.t2c.persistence.entities.CashEntity;
import net.sparkeek.t2c.rest.queries.QueryFactory;
import net.sparkeek.t2c.rest.queries.QueryGetCash;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import autodagger.AutoInjector;
import hugo.weaving.DebugLog;
import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Adrien on 16/11/2016.
 */
@AutoInjector(ApplicationAndroidStarter.class)
public class PresenterCashDisplay extends MvpBasePresenter<CashDisplayMvp.View> implements CashDisplayMvp.Presenter {

    //region Injected fields
    @Inject
    Context context;
    @Inject
    EventBus eventBus;
    @Inject
    SingleEntityStore<Persistable> dataStore;
    @Inject
    QueryFactory queryFactory;
    //endregion

    //regions Fields
    private Subscription mSubscriptionGetCash;
    //endregion

    //region Constructor
    public PresenterCashDisplay(){
        ApplicationAndroidStarter.sharedApplication().componentApplication().inject(this);
    }

    //region Overridden method

    @Override
    public void attachView(final CashDisplayMvp.View poView){
        super.attachView(poView);
        eventBus.register(this);
    }

    @Override
    public void detachView(final boolean pbRetainInstance) {
        super.detachView(pbRetainInstance);
        if (!pbRetainInstance) {
            unsubscribe();
        }
        eventBus.unregister(this);
    }
    //endregion
    @Override
    public void loadCash(final boolean pbPullToRefresh) {
        startQueryGetCash(pbPullToRefresh);
    }


    private void getCash(final boolean pbPullToRefresh){
        unsubscribe();

        final CashDisplayMvp.View loView = getView();
        if(loView == null){
            return;
        }

        final ArrayList<CashEntity> lloCash = new ArrayList<>();
        mSubscriptionGetCash = dataStore.select(CashEntity.class)
                .get()
                .toObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //on next
                        (final CashEntity poCash) -> lloCash.add(poCash),
                        //on Error
                        (final Throwable poException) -> {
                            Toast.makeText(context, "Task Error", Toast.LENGTH_LONG).show();
                            if(isViewAttached()) {
                                loView.showError(poException, pbPullToRefresh);
                            }
                            unsubscribe();
                        },
                        // on Completed
                        () -> {
                            Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                            if(isViewAttached()) {
                                loView.setData(new CashDisplayMvp.Model(lloCash));
                                if(lloCash.isEmpty()) {
                                    loView.showEmpty();
                                } else {
                                    loView.showContent();
                                }
                            }
                            unsubscribe();
                        }
                );
    }

    //region Specific job
    private void unsubscribe() {
        if(mSubscriptionGetCash != null &&!mSubscriptionGetCash.isUnsubscribed()){
            mSubscriptionGetCash.unsubscribe();
        }
        mSubscriptionGetCash = null;
    }
    //endregion


    //region networkJob
    private void startQueryGetCash(final boolean pbPullToRefresh){
        final CashDisplayMvp.View loView = getView();
        if(isViewAttached() && loView != null){
            loView.showLoading(pbPullToRefresh);
        }
        queryFactory.startQueryGetCash(context, pbPullToRefresh);
    }
    //endregion

    //Region event Management
    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventQueryGetCash(@NonNull final QueryGetCash.EventQueryGetCashDidFinish poEvent){
        if(poEvent.success){
            getCash(false);
            final CashDisplayMvp.View loView = getView();
            //loView.setData(new CashDisplayMvp.Model(poEvent.results));
            loView.showContent();
            unsubscribe();
        } else {
            final CashDisplayMvp.View loView = getView();
            if(isViewAttached() && loView != null){
                loView.showError(poEvent.throwable, poEvent.pullToRefresh);
            }
        }
    }
    //endregion
}
