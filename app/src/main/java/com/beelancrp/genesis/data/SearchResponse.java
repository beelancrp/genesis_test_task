package com.beelancrp.genesis.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by beeLAN on 03.12.2016.
 */

public class SearchResponse {

    @SerializedName("queries")
    private Queries queries;

    @SerializedName("items")
    private List<SearchItem> searchItems;

    public Queries getQueries() {
        return queries;
    }

    public void setQueries(Queries queries) {
        this.queries = queries;
    }

    public List<SearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<SearchItem> searchItems) {
        this.searchItems = searchItems;
    }
}
