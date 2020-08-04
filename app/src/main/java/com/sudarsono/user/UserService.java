package com.sudarsono.user;

import com.sudarsono.user.dao.User;
import com.sudarsono.user.dao.UserPagination;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("/search/users")
    Flowable<UserPagination> getUsers(@Query("q") String name, @Query("page") int page);

    @GET("/users")
    Flowable<List<User>> getUsers();

}
