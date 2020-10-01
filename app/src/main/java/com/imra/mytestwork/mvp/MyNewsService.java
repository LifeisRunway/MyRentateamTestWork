package com.imra.mytestwork.mvp;

import com.imra.mytestwork.app.MyRentateamApi;
import com.imra.mytestwork.mvp.models.UserList;
import io.reactivex.Observable;

public class MyNewsService {

    private MyRentateamApi myRentateamApi;

    public MyNewsService (MyRentateamApi myRentateamApi) {this.myRentateamApi = myRentateamApi;}

    public Observable<UserList> getUsers() {
        return myRentateamApi.getUsers();
    }
}
