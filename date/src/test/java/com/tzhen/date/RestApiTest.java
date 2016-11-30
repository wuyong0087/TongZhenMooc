package com.tzhen.date;

import com.tongzhen.mooc.entities.LoginJson;
import com.tzhen.mooc.internal.di.DaggerDataComponent;
import com.tzhen.mooc.internal.di.DataModule;
import com.tzhen.mooc.net.drupal.ConfigEndpoints;
import com.tzhen.mooc.net.drupal.Endpoints;
import com.tzhen.mooc.net.drupal.RestApi;
import com.tzhen.mooc.storage.Persistence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RestApiTest {
    private RestApi restApi;
    @Mock
    private Persistence persistence;

    @Before
    public void setUp() {
        Endpoints endpoints = DaggerDataComponent.builder().dataModule(new DataModule(ConfigEndpoints.BASE_URL)).build().endpoints();
        restApi = new RestApi(endpoints, persistence);
    }

    @Test
    public void login(){
        TestSubscriber<LoginJson> subscriber = new TestSubscriber<>();

        String mobile = "18800000000";
        String password = "qqqqqq";
        restApi.login(mobile, password).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_info(){
        TestSubscriber<LoginJson> subscriber = new TestSubscriber<>();

        String uid = "qqqqqq";
        restApi.my_info(uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }
}