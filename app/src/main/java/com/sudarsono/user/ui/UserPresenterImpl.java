package com.sudarsono.user.ui;

import com.sudarsono.user.util.schedulers.BaseSchedulerProvider;

import java.io.IOException;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

public class UserPresenterImpl implements UserPresenter {

    private UserDataSource dataSource;

    private UserView view;

    private BaseSchedulerProvider schedulerProvider;

    private CompositeDisposable compositeDisposable;

    public UserPresenterImpl(UserDataSource dataSource, UserView view, BaseSchedulerProvider schedulerProvider) {
        this.dataSource = dataSource;
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void findNextPageUsers(String name) {
        this.compositeDisposable.clear();
        if(name == null || name.isEmpty()) {
            findRandomUsers();
        } else {
            findUsers(name);
        }
    }

    @Override
    public void findFirstPageUsers(String name) {
        this.dataSource.resetPage();
        this.compositeDisposable.clear();
        if(name == null || name.isEmpty()) {
            findRandomUsers();
        } else {
            findUsers(name);
        }
    }

    private void findUsers(String name) {
        this.compositeDisposable.add(dataSource.requestUsers(name)
                .subscribeOn(schedulerProvider.single())
                .observeOn(schedulerProvider.ui())
                .subscribe(userPagination -> {
                    dataSource.increasePage();
                    view.showMovie(userPagination.getUsers());
                    if (userPagination.isLastPage() || userPagination.getUsers().isEmpty()) {
                        view.displayEndOfData();
                    }
                }, this::onError));
    }

    private void findRandomUsers() {
        this.compositeDisposable.clear();
        this.compositeDisposable.add(dataSource.requestUsers()
                .subscribeOn(schedulerProvider.single())
                .observeOn(schedulerProvider.ui())
                .subscribe(users -> {
                    dataSource.increasePage();
                    view.showMovie(users);
                    view.displayEndOfData();
                }, this::onError));
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        if(throwable instanceof HttpException) {
            onHttpException(throwable);
        } else if(throwable instanceof IOException) {
            view.showNetworkError();
        } else {
            view.showGeneralError("USR001", "Something went wrong");
        }
    }

    private void onHttpException(Throwable throwable) {
        HttpException error = (HttpException) throwable;
        int code = error.code();
        if(code == 403) {
            view.showLimitExceeded();
        } else {
            view.showGeneralError("USR002", "Something went wrong");
        }
    }

    @Override
    public void subscribe() {
        this.findRandomUsers();
    }

    @Override
    public void unsubscribe() {
        this.compositeDisposable.clear();
    }
}
