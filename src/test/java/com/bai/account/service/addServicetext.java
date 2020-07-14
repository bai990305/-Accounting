package com.bai.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

public class addServicetext {
    private addService add;
    @BeforeEach
    public void setup(){
        add=new addService();
    }
    @Test
    void testAddMethod(){
        int num=100;
        assertEquals(101,add.addOne(100));
        List<Integer> result=new ArrayList();
        assertThat(result).isNotEmpty();
    }
}
