package com.sudarsono.user.ui.users;

import com.sudarsono.user.UserService;
import com.sudarsono.user.contract.ApiContract;
import com.sudarsono.user.dto.User;
import com.sudarsono.user.dto.UserPagination;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDataSource {

    private UserService userService;

    private int page;

    public UserDataSource() {
        this.userService = provideUserService();
        this.page = 1;
    }

    private UserService provideUserService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContract.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
        return retrofit.create(UserService.class);
    }

    public Flowable<UserPagination> requestUsers(String query) {
        return userService.getUsers(query, page);
    }

    public Flowable<List<User>> requestUsers() {
        return userService.getUsers();
    }

    public void increasePage() {
        this.page++;
    }

    public void resetPage() {
        this.page = 1;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public int getPage() {
        return page;
    }
}
