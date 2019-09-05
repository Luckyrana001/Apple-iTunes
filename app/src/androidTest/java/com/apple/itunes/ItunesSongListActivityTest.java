package com.apple.itunes;

import android.content.Intent;
import android.view.View;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.apple.itunes.view.ItunesSongsListActivity;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.apple.itunes.controller.services.helper.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

public class ItunesSongListActivityTest {

    @Rule
    public ActivityTestRule<ItunesSongsListActivity> mActivityTestRule = new ActivityTestRule<ItunesSongsListActivity>(ItunesSongsListActivity.class);

    private ItunesSongsListActivity mActivity = null;
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();

        InstrumentationRegistry.getInstrumentation();
        server = new MockWebServer();
        server.start();
        Constants.BASE_URL = server.url("/").toString();
    }

    @Test
    public void testSpinnerViewExistOrNot() {
        View view = mActivity.findViewById(R.id.no_data_found_tv);
        assertNotNull(view);
    }


    @Test
    public void testRandomDataIsShown() throws Exception {
        String fileName = "apple_itunes_api_200_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.no_data_found_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

    }


    @Test
    public void testRandomDataRetrieval() throws Exception {

        MockAdapterTest quoteOfTheDayMockAdapterTest = new MockAdapterTest();
        quoteOfTheDayMockAdapterTest.testRandomDataRetrieval();
    }

    @Test
    public void testRetryButtonShowsWhenError() throws Exception {
        String fileName = "apple_itunes_api_401_not_found.json";

        server.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody(RestServiceTestHelper.getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

    }

    /**
     * Initial values testing.
     */
    @Test
    public void checkCityValueTest() {
        ViewInteraction cityValueTest = onView(withId(R.id.label_tv));
        cityValueTest.check(matches(withText("Search Songs By Author Names,Title Track...")));
    }

    @After
    public void tearDown() throws Exception {
    }
}