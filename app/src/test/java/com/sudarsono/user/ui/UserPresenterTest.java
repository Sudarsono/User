package com.sudarsono.user.ui;

import com.sudarsono.user.dao.User;
import com.sudarsono.user.dao.UserPagination;
import com.sudarsono.user.util.schedulers.BaseSchedulerProvider;
import com.sudarsono.user.util.schedulers.ImmediateSchedulerProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserPresenterTest {

    @Mock
    UserView view;

    @Mock
    UserDataSource dataSource;

    private BaseSchedulerProvider schedulerProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        schedulerProvider = new ImmediateSchedulerProvider();
    }

    @After
    public void tearDown() {
        Mockito.validateMockitoUsage();
    }

    @Test
    public void testFindNextPageUsers_emptyResultSuccess() {
        List<User> users = new ArrayList<>();
        when(dataSource.requestUsers()).thenReturn(Flowable.just(users));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findNextPageUsers("");
        verify(view).showMovie(users);
        verify(view).displayEndOfData();
        verify(dataSource).increasePage();
    }

    @Test
    public void testFindNextPageUsers_withResultSuccess() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(dataSource.requestUsers()).thenReturn(Flowable.just(users));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findNextPageUsers(null);
        verify(view).showMovie(users);
        verify(view).displayEndOfData();
        verify(dataSource).increasePage();
    }

    @Test
    public void testFindNextPageUsers_filterNameWithResultSuccess() {
        UserPagination pagination = new UserPagination();
        List<User> users = new ArrayList<>();
        users.add(new User());
        pagination.setUsers(users);

        when(dataSource.requestUsers("John")).thenReturn(Flowable.just(pagination));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findNextPageUsers("John");
        verify(view).showMovie(users);
        verify(view, never()).displayEndOfData();
        verify(dataSource).increasePage();
    }

    @Test
    public void testFindNextPageUsers_filterEmptyResultSuccess() {
        UserPagination pagination = new UserPagination();
        List<User> users = new ArrayList<>();
        pagination.setUsers(users);

        when(dataSource.requestUsers("John")).thenReturn(Flowable.just(pagination));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findNextPageUsers("John");
        verify(view).showMovie(users);
        verify(view).displayEndOfData();
        verify(dataSource).increasePage();
    }

    @Test
    public void testFindNextPageUsers_endDataSuccess() {
        UserPagination pagination = new UserPagination();
        List<User> users = new ArrayList<>();
        users.add(new User());
        pagination.setLastPage(true);
        pagination.setUsers(users);

        when(dataSource.requestUsers("John")).thenReturn(Flowable.just(pagination));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findNextPageUsers("John");
        verify(view).showMovie(users);
        verify(view).displayEndOfData();
        verify(dataSource).increasePage();
    }

    @Test
    public void testFindFirstPageUsers_emptyResultSuccess() {
        List<User> users = new ArrayList<>();
        when(dataSource.requestUsers()).thenReturn(Flowable.just(users));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findFirstPageUsers("");
        verify(view).showMovie(users);
        verify(view).displayEndOfData();
        verify(dataSource).resetPage();
    }

    @Test
    public void testFindFirstPageUsers_withResultSuccess() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(dataSource.requestUsers()).thenReturn(Flowable.just(users));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findFirstPageUsers(null);
        verify(view).showMovie(users);
        verify(view).displayEndOfData();
        verify(dataSource).resetPage();
    }

    @Test
    public void testFindFirstPageUsers_filterNameWithResultSuccess() {
        UserPagination pagination = new UserPagination();
        List<User> users = new ArrayList<>();
        users.add(new User());
        pagination.setUsers(users);

        when(dataSource.requestUsers("John")).thenReturn(Flowable.just(pagination));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findFirstPageUsers("John");
        verify(view).showMovie(users);
        verify(view, never()).displayEndOfData();
        verify(dataSource).resetPage();
    }

    @Test
    public void testFindFirstPageUsers_filterEmptyResultSuccess() {
        UserPagination pagination = new UserPagination();
        List<User> users = new ArrayList<>();
        pagination.setUsers(users);

        when(dataSource.requestUsers("John")).thenReturn(Flowable.just(pagination));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findFirstPageUsers("John");
        verify(view).showMovie(users);
        verify(view).displayEndOfData();
        verify(dataSource).resetPage();
    }

    @Test
    public void testFindFirstPageUsers_endDataSuccess() {
        UserPagination pagination = new UserPagination();
        List<User> users = new ArrayList<>();
        users.add(new User());
        pagination.setLastPage(true);
        pagination.setUsers(users);

        when(dataSource.requestUsers("John")).thenReturn(Flowable.just(pagination));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findFirstPageUsers("John");
        verify(view).showMovie(users);
        verify(view).displayEndOfData();
        verify(dataSource).resetPage();
    }

    @Test
    public void testRequestFirstPageUsers_ioException() {
        when(dataSource.requestUsers("John")).thenReturn(Flowable.fromCallable(() -> {
            throw new IOException();
        }));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findFirstPageUsers("John");
        verify(view).showNetworkError();
    }

    @Test
    public void testRequestFirstPageUsers_generalException() {
        when(dataSource.requestUsers("John")).thenReturn(Flowable.fromCallable(() -> {
            throw new Exception();
        }));
        UserPresenterImpl presenter = new UserPresenterImpl(dataSource, view, schedulerProvider);
        presenter.findFirstPageUsers("John");
        verify(view).showGeneralError("USR001", "Something went wrong");
    }

}
