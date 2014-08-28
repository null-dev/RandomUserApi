package com.terro.services;

import com.terro.BaseTestCase;
import com.terro.Gender;
import com.terro.entities.Result;
import com.terro.entities.User;
import com.terro.entities.UserRandomResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.fest.assertions.api.Assertions.assertThat;

public class UserServicesAsyncTest extends BaseTestCase {

    private CountDownLatch lock = new CountDownLatch(1);
    private UserRandomResponse responseUserRandom;


    public void testUserAsync() throws Exception {
        getManager().userServicesAsync().userAsync(new Callback<UserRandomResponse>() {
            @Override
            public void success(UserRandomResponse userRandomResponse, Response response) {
                responseUserRandom = userRandomResponse;
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

        lockAwait();
        assertThat(responseUserRandom).isNotNull();
        assertThat(responseUserRandom.getResults().size()).isEqualTo(1);
    }

    public void testListUserAsync() throws Exception {
        getManager().userServicesAsync().listUserAsync(20, new Callback<UserRandomResponse>() {
            @Override
            public void success(UserRandomResponse userRandomResponse, Response response) {
                responseUserRandom = userRandomResponse;
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

        lockAwait();
        assertThat(responseUserRandom).isNotNull();
        assertThat(responseUserRandom.getResults().size()).isEqualTo(20);
    }

    public void testUserWithGenderAsync() throws Exception {
        getManager().userServicesAsync().userWithGenderAsync(Gender.FEMALE, new Callback<UserRandomResponse>() {
            @Override
            public void success(UserRandomResponse userRandomResponse, Response response) {
                responseUserRandom = userRandomResponse;
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

        lockAwait();
        assertThat(this.responseUserRandom).isNotNull();
        assertThat(this.responseUserRandom.getResults().get(0).getUser().getGender()).isEqualTo(Gender.FEMALE);
    }

    public void testListUsersWithGenderAsync() throws Exception {
        getManager().userServicesAsync().listUsersWithGenderAsync(20, Gender.FEMALE, new Callback<UserRandomResponse>() {
            @Override
            public void success(UserRandomResponse userRandomResponse, Response response) {
                responseUserRandom = userRandomResponse;
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

        lockAwait();
        assertThat(responseUserRandom).isNotNull();
        assertThat(responseUserRandom.getResults().size()).isEqualTo(20);

        for (Result resultFemale : responseUserRandom.getResults()) {
            User user = resultFemale.getUser();
            assertThat(user).isNotNull();
            assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
        }
    }

    private void lockAwait () {
        try {
            lock.await(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}