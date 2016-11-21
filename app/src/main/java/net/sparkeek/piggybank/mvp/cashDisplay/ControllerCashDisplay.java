package net.sparkeek.piggybank.mvp.cashDisplay;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby.conductor.viewstate.MvpViewStateController;

import net.sparkeek.piggybank.R;
import net.sparkeek.piggybank.persistence.entities.Cash;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.aprilapps.switcher.Switcher;

/**
 * Created by Adrien on 16/11/2016.
 */

public class ControllerCashDisplay
        extends MvpViewStateController<CashDisplayMvp.View, PresenterCashDisplay, CashDisplayMvp.ViewState>
        implements CashDisplayMvp.View {

    //region fields
    private Switcher mSwitcher;
    //endregion

    //region injected views
    @Bind(R.id.ControllerCashDisplay_TextView_Description)
    TextView mTextViewDescription;
    @Bind(R.id.ControllerCashDisplay_TextView_Url)
    TextView mTextViewUrl;
    @Bind(R.id.ControllerCashDisplay_TextView_Empty)
    TextView mTextViewEmpty;
    @Bind(R.id.ControllerCashDisplay_TextView_Error)
    TextView mTextViewError;
    @Bind(R.id.ControllerCashDisplay_ProgressBar_Loading)
    ProgressBar mProgressBarLoading;
    @Bind(R.id.ControllerCashDisplay_ContentView)
    LinearLayout mContentView;
    //endregion

    //region default constructor
    public ControllerCashDisplay() {  }
    //endregion


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater poInflater, @NonNull ViewGroup poContainer) {
        final View loView = poInflater.inflate(R.layout.controller_cash_display, poContainer, false);

        ButterKnife.bind(this, loView);

        mSwitcher = new Switcher.Builder()
                .withEmptyView(mTextViewEmpty)
                .withProgressView(mProgressBarLoading)
                .withErrorView(mTextViewError)
                .withContentView(mContentView)
                .build();

        return loView;
    }

    @NonNull
    @Override
    public CashDisplayMvp.ViewState createViewState() {
        return new CashDisplayMvp.ViewState();
    }

    @Override
    public void onViewStateInstanceRestored(boolean pbnstanceStateRetained) {

    }

    @Override
    public void onNewViewStateInstance() {
        loadData(false);
    }

    //region MvpViewStateController
    @NonNull
    @Override
    public PresenterCashDisplay createPresenter() {
        return new PresenterCashDisplay();
    }
    //endregion

    //region ViewCashDisplay
    @Override
    public void showEmpty() {
        mSwitcher.showEmptyView();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        mSwitcher.showProgressView();
    }

    @Override
    public void showContent() {
        mSwitcher.showContentView();
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        mSwitcher.showErrorView();
    }

    @Override
    public void setData(CashDisplayMvp.Model poCash) {
        configureViewWithCash(poCash.cash.get(0));
    }

    @Override
    public void loadData(boolean pullToRefresh) {
            getPresenter().loadCash(pullToRefresh);
    }

    private void configureViewWithCash(@NonNull final Cash poCash){
        mTextViewDescription.setText("Solde : " + poCash.getCash());
    }
}
