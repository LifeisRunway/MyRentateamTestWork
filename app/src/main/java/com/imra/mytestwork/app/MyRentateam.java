package com.imra.mytestwork.app;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.multidex.MultiDexApplication;

import com.imra.mytestwork.di.AppComponent;
import com.imra.mytestwork.di.DaggerAppComponent;
import com.imra.mytestwork.di.modules.ContextModule;

public class MyRentateam extends MultiDexApplication {

    private static AppComponent mAppComp;

    @Override
    public void onCreate () {
        super.onCreate();

        mAppComp = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return mAppComp;
    }


    @VisibleForTesting
    public static void setAppComponent(@NonNull AppComponent appComponent) {
        mAppComp = appComponent;
    }

}
