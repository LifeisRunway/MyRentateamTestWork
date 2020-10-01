package com.imra.mytestwork.di.modules;

import com.imra.mytestwork.app.MyRentateamApi;
import com.imra.mytestwork.mvp.MyNewsService;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module(includes = {ApiModules.class})
public class MyNewsModule {

    @Provides
    @Singleton
    public MyNewsService provideMyNewsService (MyRentateamApi authApi) {return new MyNewsService(authApi);}

}
