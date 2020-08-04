package com.sudarsono.user.ui;

import com.sudarsono.user.dao.User;
import com.sudarsono.user.util.BaseView;

import java.util.List;

public interface UserView  extends BaseView<UserPresenter> {

    void showMovie(List<User> users);

    void displayEndOfData();

    void showLimitExceeded();

    void showNetworkError();

}
