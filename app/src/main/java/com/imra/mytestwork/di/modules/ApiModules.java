package com.imra.mytestwork.di.modules;

import com.imra.mytestwork.app.MyRentateamApi;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = {RetrofitModule.class})
public class ApiModules {

    @Provides
    @Singleton
    public MyRentateamApi provideAuthApi(Retrofit retrofit) {return retrofit.create(MyRentateamApi.class);}

}
