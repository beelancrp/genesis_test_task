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

    private List<SearchResponse> mResponse;

    public SearchThread(String searchText, String searchCode, String apiKey, int startPosition) {
        this.searchText = searchText;
        this.searchCode = searchCode;
        this.apiKey = apiKey;
        this.startPosition = startPosition;
        this.mResponse = new ArrayList<>();

        mAPISearchService = UniversalAPIService.createRetrofitService(APISearchService.class);
    }

    @Override
    public List<SearchItem> call() throws Exception {

        mResponse.add(doRequest(searchText, startPosition, 10));
        mResponse.add(doRequest(searchText, startPosition + 10, 5));

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

    private List<SearchItem> setSearchItems(List<SearchResponse> items) {
        List<SearchItem> tempList = new ArrayList<>();
        for (SearchResponse item : items) {
            try {
                if (item.getQueries().getNextPage().get(0).getTotalResults().equals("0")) {
                    return tempList;
                }
                tempList.addAll(item.getSearchItems());
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
        return tempList;
    }

}
