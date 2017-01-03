package com.beelancrp.genesis.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by yaroslavzaklinsky on 12/22/16.
 */
public class SearchItemTest {
    @Test
    public void equals() throws Exception {
        SearchItem i1 = new SearchItem("111", "222");
        SearchItem i2 = new SearchItem("111", "222");

        assertEquals(i1, i2);
    }

}