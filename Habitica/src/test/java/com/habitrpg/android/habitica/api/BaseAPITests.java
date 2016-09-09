package com.habitrpg.android.habitica.api;


import com.habitrpg.android.habitica.old.APIHelper;
import com.habitrpg.android.habitica.BuildConfig;
import com.habitrpg.android.habitica.old.HostConfig;
import com.magicmicky.habitrpgwrapper.lib.models.UserAuthResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.security.InvalidParameterException;
import java.util.UUID;

import rx.observers.TestSubscriber;

public class BaseAPITests {

    public APIHelper apiHelper;
    public HostConfig hostConfig;

    public String username;
    public final String password = "password";

    @Before
    public void setUp() {
        if (BuildConfig.BASE_URL.contains("habitica.com")) {
            throw new InvalidParameterException("Can't test against production server.");
        }
        hostConfig = new HostConfig(BuildConfig.BASE_URL,
                BuildConfig.PORT,
                "",
                "");
        apiHelper = new APIHelper(APIHelper.createGsonFactory(), hostConfig);
        generateUser();
    }

    public void generateUser() {
        TestSubscriber<UserAuthResponse> testSubscriber = new TestSubscriber<>();
        username = UUID.randomUUID().toString();
        apiHelper.registerUser(username, username+"@example.com", password, password)
        .subscribe(testSubscriber);
        testSubscriber.assertCompleted();
        UserAuthResponse response = testSubscriber.getOnNextEvents().get(0);
        hostConfig.setUser(response.getId());
        hostConfig.setApi(response.getApiToken() != null ? response.getApiToken() : response.getToken());
    }

    public HabitRPGUser getUser() {
        TestSubscriber<HabitRPGUser> userSubscriber = new TestSubscriber<>();

        apiHelper.apiService.getUser().subscribe(userSubscriber);
        userSubscriber.assertNoErrors();
        userSubscriber.assertCompleted();
        List<HabitRPGUser> users = userSubscriber.getOnNextEvents();

        return users.get(0);
    }

    @After
    public void tearDown() {
    }
}
