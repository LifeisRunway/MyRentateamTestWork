package com.imra.mytestwork.mvp.presenters;

import androidx.annotation.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import moxy.MvpPresenter;
import moxy.MvpView;

public class BasePresenter<View extends MvpView> extends MvpPresenter<View> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
