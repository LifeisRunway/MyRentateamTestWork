package com.imra.mytestwork.mvp.presenters;

import com.imra.mytestwork.app.MyRentateam;
import com.imra.mytestwork.di.common.ArticleDao;
import com.imra.mytestwork.mvp.MyNewsService;
import com.imra.mytestwork.mvp.models.User;
import com.imra.mytestwork.mvp.models.UserList;
import com.imra.mytestwork.mvp.views.RepositoriesView;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;

@Singleton
@InjectViewState
public class RepositoriesPresenter extends BasePresenter<RepositoriesView>{

    @Inject
    MyNewsService myNewsService;

    @Inject
    ArticleDao mAD;

    private boolean mIsInLoading;

    public RepositoriesPresenter() {
        MyRentateam.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach () {
        super.onFirstViewAttach();
        getViewState().onFirstAttach();
    }

    public void loadRepositories (boolean isRefreshing, boolean isConnected) {
        loadData(false, isRefreshing, isConnected);
    }

    private void loadData (boolean isPageLoading, boolean isRefreshing, boolean isConnected) {

        if (mIsInLoading) {
            return;
        }
        mIsInLoading = true;

        getViewState().onStartLoading();

        showProgress(isPageLoading, isRefreshing);
        if(isConnected) {
            Observable<UserList> observable = myNewsService.getUsers();

            Disposable disposable = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((UserList users) -> {
                        onLoadingFinish(isPageLoading, isRefreshing);
                        saveUserInDB(users.getUserList());
                        onLoadingSuccess(isPageLoading, getUsersInDB());
                    }, error -> {
                        onLoadingFinish(isPageLoading, isRefreshing);
                        onLoadingFailed(error);
                    });
            unsubscribeOnDestroy(disposable);
        } else {
            Observable<List<User>> observable = Observable.just(getUsersInDB());

            Disposable disposable = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((List<User> users) -> {
                        onLoadingFinish(isPageLoading, isRefreshing);
                        onLoadingSuccess(isPageLoading, users);
                        getViewState().withoutInternetMessage();
                    }, error -> {
                        onLoadingFinish(isPageLoading, isRefreshing);
                        onLoadingFailed(error);
                    });
            unsubscribeOnDestroy(disposable);
        }
    }

    private List<User> getUsersInDB() {
        return mAD.getAllUsers();
    }

    private void saveUserInDB(List<User> users) {
        mAD.insertOrUpdateUsers(users);
    }

    private void onLoadingFinish(boolean isPageLoading, boolean isRefreshing) {
        mIsInLoading = false;

        getViewState().onFinishLoading();

        hideProgress(isPageLoading, isRefreshing);
    }

    private void onLoadingSuccess (boolean isPageLoading, List<User> users) {

        if (isPageLoading) {
            getViewState().addRepositories(users);
        } else {
            getViewState().setRepositories(users);
        }
    }

    private void onLoadingFailed(Throwable error) {
        getViewState().showError(error.toString());
    }

    public void onErrorCancel() {
        getViewState().hideError();
    }

    private void showProgress(boolean isPageLoading, boolean isRefreshing) {
        if (isPageLoading) {
            return;
        }

        if (isRefreshing) {
            getViewState().showRefreshing();
        } else {
            getViewState().showListProgress();
        }
    }

    private void hideProgress(boolean isPageLoading, boolean isRefreshing) {
        if (isPageLoading) {
            return;
        }

        if (isRefreshing) {
            getViewState().hideRefreshing();
        } else {
            getViewState().hideListProgress();
        }
    }
}
