package com.imra.mytestwork.mvp.presenters;

import com.imra.mytestwork.mvp.models.User;
import com.imra.mytestwork.mvp.views.MainInterface;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainInterface> {

    public void onRepositorySelection (int position, User user) {
        getViewState().setSelection(position);

        getViewState().showDetails(position, user);

        getViewState().showDetailsContainer(position);
    }
}
