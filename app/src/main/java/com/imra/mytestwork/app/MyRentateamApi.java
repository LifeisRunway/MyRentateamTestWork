package com.imra.mytestwork.app;

import com.imra.mytestwork.mvp.models.UserList;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Date: 27.07.2019
 * Time: 17:25
 *
 * @author IMRA027
 */

public interface MyRentateamApi {
    @GET("users")
    Observable<UserList> getUsers();
}
