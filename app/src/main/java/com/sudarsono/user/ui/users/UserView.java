package com.sudarsono.user.ui.users;

import com.sudarsono.user.dto.User;
import com.sudarsono.user.util.BaseView;

import java.util.List;

public interface UserView  extends BaseView<UserPresenter> {

    void showMovie(List<User> users);

    void displayEndOfData();

    void showLimitExceeded();

    void showNetworkError();

}
