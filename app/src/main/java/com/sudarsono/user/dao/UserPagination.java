package com.sudarsono.user.dao;

import com.google.gson.annotations.SerializedName;
import com.sudarsono.user.dao.User;

import java.util.List;

public class UserPagination {

    @SerializedName("incomplete_results")
    private boolean lastPage;

    @SerializedName("items")
    private List<User> users;

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
