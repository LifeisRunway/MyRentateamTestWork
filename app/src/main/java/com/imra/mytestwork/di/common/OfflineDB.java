package com.imra.mytestwork.di.common;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.imra.mytestwork.mvp.models.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class OfflineDB extends RoomDatabase{

    public abstract ArticleDao articleDao();

    public static final String DATABASE_NAME = "mDB.db";
}
