package com.imra.mytestwork.di;

import android.content.Context;
import com.imra.mytestwork.di.common.ArticleDao;
import com.imra.mytestwork.di.modules.ContextModule;
import com.imra.mytestwork.di.modules.MyNewsModule;
import com.imra.mytestwork.di.modules.OfflineDBModule;
import com.imra.mytestwork.mvp.MyNewsService;
import com.imra.mytestwork.mvp.presenters.RepositoriesPresenter;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component (modules = {ContextModule.class, MyNewsModule.class, OfflineDBModule.class})
public interface AppComponent {

    Context getContext();
    MyNewsService getMyNewsService();
    ArticleDao getAD();

    void inject(RepositoriesPresenter presenter);
}
