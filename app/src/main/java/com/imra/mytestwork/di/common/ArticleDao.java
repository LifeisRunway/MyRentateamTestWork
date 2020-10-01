package com.imra.mytestwork.di.common;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import com.imra.mytestwork.mvp.models.User;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE id = :id")
    User getUser(Integer id);

    @Transaction
    default void insertOrUpdateUsers (List<User> users) {
        List<Long> insertResult = insertUsers(users);
        List<User> updateList = new ArrayList<User>();

        for(int i = 0; i < insertResult.size(); i++) {
            if(insertResult.get(i) == -1L) {updateList.add(users.get(i));}
        }

        if(!updateList.isEmpty()) {
            updateUsers(updateList);
        }
    }

    @Update
    void updateUsers(List<User> users);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertUsers(List<User> users);

}
