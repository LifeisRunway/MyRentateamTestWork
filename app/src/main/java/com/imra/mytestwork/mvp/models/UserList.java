package com.imra.mytestwork.mvp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserList {

    @SerializedName("data")
    List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> usersList) {
        this.userList = usersList;
    }
}
