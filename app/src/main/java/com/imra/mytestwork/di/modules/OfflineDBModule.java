package com.imra.mytestwork.di.modules;

import androidx.room.Room;
import android.content.Context;
import com.imra.mytestwork.di.common.ArticleDao;
import com.imra.mytestwork.di.common.OfflineDB;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class OfflineDBModule {

    @Provides
    @Singleton
    public OfflineDB provideOfflineDB (Context context) {
        return Room.databaseBuilder(context, OfflineDB.class, OfflineDB.DATABASE_NAME).allowMainThreadQueries().build();
    }

    @Provides
    @Singleton
    public ArticleDao provideArticleDAO (OfflineDB offlineDB) {
        return offlineDB.articleDao();
    }

}
