package com.sudarsono.user.ui;

import com.sudarsono.user.util.BasePresenter;

public interface UserPresenter extends BasePresenter {

    void findFirstPageUsers(String name);

    void findNextPageUsers(String name);

}
