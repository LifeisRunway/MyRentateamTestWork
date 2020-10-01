package com.imra.mytestwork.mvp.views;

import com.imra.mytestwork.mvp.models.User;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainInterface extends MvpView {

    void setSelection(int position);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDetailsContainer(int position);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDetails(int position, User user);

}
