package com.beelancrp.genesis.services.search;

import com.beelancrp.genesis.data.SearchItem;

import java.util.List;

/**
 * Created by yaroslavzaklinsky on 12/22/16.
 */

public interface SearchService {

    void search(String term);
    void cancelSearch();
    List<SearchItem> getResults();

}
