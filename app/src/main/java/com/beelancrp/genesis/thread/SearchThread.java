package com.beelancrp.genesis.thread;

import com.beelancrp.genesis.api.APISearchService;
import com.beelancrp.genesis.api.UniversalAPIService;
import com.beelancrp.genesis.data.SearchItem;
import com.beelancrp.genesis.data.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class SearchThread implements Callable<List<SearchItem>> {

    private APISearchService mAPISearchService;

    private String searchText;
    private String searchCode;
    private String apiKey;
    private int startPosition;
    private int offset;

    public SearchThread(String searchText, int startPosition, int offset) {
        this.searchText = searchText;
        this.searchCode = "005254917935128226642:itevvomlo04";
        this.apiKey = "AIzaSyDHW1waQw1aM8cQ8r8w-cZ9ngX5rXY1-ao";
        this.startPosition = startPosition;
        this.offset = offset;

        mAPISearchService = UniversalAPIService.createRetrofitService(APISearchService.class);
    }

    @Override
    public List<SearchItem> call() throws Exception {

        SearchResponse mResponse = doRequest(searchText, startPosition, offset);

        return setSearchItems(mResponse);
    }

    private SearchResponse doRequest(String searchText, int startPosition, int offset) {
        try {
            return mAPISearchService.getSearchResponse(searchText,
                    searchCode,
                    apiKey,
                    startPosition,
                    offset).execute().body();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<SearchItem> setSearchItems(SearchResponse item) {
        List<SearchItem> tempList = new ArrayList<>();
        try {
            if (item.getQueries().getNextPage().get(0).getTotalResults().equals("0")) {
                return tempList;
            }
            tempList.addAll(item.getSearchItems());
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();

        }
        return tempList;
    }

}
