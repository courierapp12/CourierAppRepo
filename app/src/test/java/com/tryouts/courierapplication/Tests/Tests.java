package com.tryouts.courierapplication.Tests;

import com.tryouts.courierapplication.interactors.NewOrderInteractor;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class Tests {

    @Mock
    NewOrderInteractor newOrderInteractor;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void validateIfNewOrderIsReady() {
        Assert.assertEquals(false, newOrderInteractor.isOrderReadyToSubmit());
    }
}
