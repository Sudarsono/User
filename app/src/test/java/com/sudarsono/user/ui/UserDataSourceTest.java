package com.sudarsono.user.ui;

import com.sudarsono.user.UserService;
import com.sudarsono.user.dao.User;
import com.sudarsono.user.dao.UserPagination;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UserDataSourceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        Mockito.validateMockitoUsage();
    }

    @Mock
    UserService userService;

    @Test
    public void testRequestUser_success() {
        List<User> users = new ArrayList<>();
        when(userService.getUsers()).thenReturn(Flowable.just(users));

        UserDataSource dataSource = new UserDataSource();
        dataSource.setUserService(userService);
        TestSubscriber<List<User>> subscriber = dataSource.requestUsers().test();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        subscriber.assertValue(users);
    }

    @Test
    public void testRequestUser_filterSuccess() {
        UserPagination pagination = new UserPagination();
        when(userService.getUsers("John", 1)).thenReturn(Flowable.just(pagination));

        UserDataSource dataSource = new UserDataSource();
        dataSource.setUserService(userService);
        TestSubscriber<UserPagination> subscriber = dataSource.requestUsers("John").test();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        subscriber.assertValue(pagination);
    }

    @Test
    public void testResetPage_success() {
        UserDataSource dataSource = new UserDataSource();
        dataSource.resetPage();
        assertEquals(1, dataSource.getPage());

        dataSource.increasePage();
        assertEquals(2, dataSource.getPage());
    }
}
