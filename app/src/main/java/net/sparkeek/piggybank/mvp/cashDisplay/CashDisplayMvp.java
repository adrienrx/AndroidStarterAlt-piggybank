package net.sparkeek.piggybank.mvp.cashDisplay;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

import net.sparkeek.piggybank.persistence.entities.CashEntity;

import java.io.Serializable;
import java.util.List;

import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by Adrien on 16/11/2016.
 */

public interface CashDisplayMvp {

    //region Model
    final class Model implements Serializable {
        public final List<CashEntity> cash;

        public Model (final List<CashEntity> poCash) { cash = poCash;}
    }
    //endregion

    //region View
    interface View extends MvpLceView<CashDisplayMvp.Model> {
        void showEmpty();
    }
    //endregion

    //region presenter
    interface Presenter extends MvpPresenter<View> {
        void loadCash(final boolean pbPullToRefresh);
    }
    //endregion

    //region ViewState
    final class ViewState implements RestorableViewState<View> {

        //region Data to retain
        @Icicle
        public Serializable data;
        //endregion

        @Override
        public void apply(final View poView, final boolean pbRetained) {
            if (data instanceof Model){
                final Model loData = (Model) data;
                poView.setData(loData);
                if (loData.cash == null || loData.cash.isEmpty()){
                    poView.showEmpty();
                } else {
                    poView.showContent();
                }
            } else {
                poView.showError(null, false);
            }
        }

        //region RestorableViewState
        @Override
        public void saveInstanceState(@NonNull final Bundle poOut) {
            Icepick.saveInstanceState(this, poOut);
        }

        @Override
        public RestorableViewState<CashDisplayMvp.View> restoreInstanceState(final Bundle poIn) {
            if (poIn == null) {
                return null;
            }
            Icepick.restoreInstanceState(this, poIn);
            return this;
        }
        //endregion

    }
    //endregion

}
