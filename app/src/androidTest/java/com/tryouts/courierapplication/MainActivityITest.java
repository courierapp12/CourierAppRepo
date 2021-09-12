package com.tryouts.courierapplication;


import android.view.View;

import androidx.test.runner.AndroidJUnit4;

import com.google.android.material.navigation.NavigationView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class MainActivityITest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureDrawerIsPresent() {
        MainActivity activity = rule.getActivity();
        View view = activity.findViewById(R.id.nvView);
        assertThat(view,notNullValue());
        assertThat(view, instanceOf(NavigationView.class));
        NavigationView drawer = (NavigationView) view;
        assertThat(drawer.getMenu(), notNullValue());

    }

}
