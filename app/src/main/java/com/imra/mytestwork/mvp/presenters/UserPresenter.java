package com.imra.mytestwork.mvp.presenters;

import com.imra.mytestwork.mvp.models.User;
import com.imra.mytestwork.mvp.views.UserView;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class UserPresenter extends MvpPresenter<UserView> {

    private User mUser;
    private int mPos;

    public UserPresenter(int position, User user) {
        super();
        mUser = user;
        mPos = position;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showRepository(mPos, mUser);
    }
}
