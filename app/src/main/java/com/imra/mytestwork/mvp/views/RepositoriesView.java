package com.imra.mytestwork.mvp.views;

import com.imra.mytestwork.mvp.models.User;
import java.util.List;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.AddToEndStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface RepositoriesView extends MvpView {

    void onFirstAttach();

    void onStartLoading();

    void onFinishLoading();

    void showRefreshing();

    void hideRefreshing();

    void showListProgress();

    void hideListProgress();

    void showError(String message);

    void hideError();

    void setRepositories(List<User> users);

    @StateStrategyType(AddToEndStrategy.class)
    void addRepositories(List<User> users);

    void withoutInternetMessage ();
}
