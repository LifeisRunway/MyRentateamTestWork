package com.imra.mytestwork.mvp.views;

import com.imra.mytestwork.mvp.models.User;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface UserView extends MvpView {
    void showRepository(int position, User user);
}
