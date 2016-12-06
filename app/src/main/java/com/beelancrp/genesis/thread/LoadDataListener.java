package com.beelancrp.genesis.thread;

import com.beelancrp.genesis.data.SearchItem;

import java.util.List;

/**
 * Created by azazellj on 12/5/16.
 */

public interface LoadDataListener {
    void dataLoaded(List<SearchItem> result, String searchText);

    void dataLoadFailed(Exception e);

    void noExistingData();
}
