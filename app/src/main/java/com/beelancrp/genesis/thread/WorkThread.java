package com.beelancrp.genesis.thread;

import com.beelancrp.genesis.api.SearchService;
import com.beelancrp.genesis.api.UniversalService;
import com.beelancrp.genesis.data.SearchItem;
import com.beelancrp.genesis.data.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beeLAN on 05.12.2016.
 */

public class WorkThread extends Thread {

    private SearchService mRozetkaService;

    private String searchText;
    private String searchCode;
    private String apiKey;
    private int startPosition;

    private LoadDataListener listener;

    private List<SearchResponse> mResponse;
    private List<SearchItem> searchItems;

    public WorkThread(String searchEngineCode,
                      String apiKey,
                      int startPosition,
                      LoadDataListener listener) {
        this.searchCode = searchEngineCode;
        this.apiKey = apiKey;
        this.startPosition = startPosition;
        this.listener = listener;
        this.searchItems = new ArrayList<>();
        this.mResponse = new ArrayList<>();
        mRozetkaService = UniversalService.createRetrofitService(SearchService.class);
    }


    public void starWithParam(String param) {
        this.searchText = param;
        this.start();
    }

    @Override
    public void run() {
        mResponse.add(startSearch(searchText, startPosition, 10));
        mResponse.add(startSearch(searchText, startPosition + 10, 5));

        setSearchItems(mResponse);
    }

    private SearchResponse startSearch(String searchText, int startPosition, int offset) {
        try {
            return mRozetkaService.getSearchResponse(searchText,
                    searchCode,
                    apiKey,
                    startPosition,
                    offset).execute().body();
        } catch (IOException | NullPointerException e) {
            listener.dataLoadFailed(e);
        }
        return null;
    }

    private void setSearchItems(List<SearchResponse> items) {
        for (SearchResponse item : items) {
            try {
                if (item.getQueries().getNextPage().get(0).getTotalResults().equals("0")) {
                    listener.noExistingData();
                    break;
                }
                searchItems.addAll(item.getSearchItems());
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                listener.noExistingData();
            }

        }
        listener.dataLoaded(searchItems, searchText);
    }

    public void stopThread() {
        this.interrupt();
    }
}
