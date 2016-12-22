package com.beelancrp.genesis.services.search;

import com.beelancrp.genesis.data.SearchItem;
import com.beelancrp.genesis.services.LoadListener;
import com.beelancrp.genesis.thread.SearchThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ImplSearchService implements SearchService {

    private ExecutorService executor = Executors.newFixedThreadPool(2);
    private LoadListener loadListener;

    private List<SearchItem> results = new ArrayList<>();

    private int searchStartPos = 1;
    private int sizeOfOnePage = 10;

    public ImplSearchService(LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    @Override
    public void search(String term) {
        Future<List<SearchItem>> result1 = executor.submit(new SearchThread(term, searchStartPos, sizeOfOnePage));
        Future<List<SearchItem>> result2 = executor.submit(new SearchThread(term, searchStartPos + sizeOfOnePage, sizeOfOnePage));

        {
            try {
                results.addAll(result1.get());
                results.addAll(result2.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                loadListener.dataLoadFailed(e);
            }
        }
        if (results.size() != 0){
            loadListener.dataLoaded(results);
        }else {
            loadListener.noExistingData();
        }
        this.cancelSearch();
    }

    @Override
    public void cancelSearch() {
        executor.shutdown();
    }

    @Override
    public List<SearchItem> getResults() {
        return results;
    }

    public void setSearchStartPos(int searchStartPos) {
        this.searchStartPos = searchStartPos;
    }

    public void setSizeOfOnePage(int sizeOfOnePage) {
        this.sizeOfOnePage = sizeOfOnePage;
    }
}
