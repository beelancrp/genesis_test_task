package com.beelancrp.genesis.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by beeLAN on 03.12.2016.
 */

public class NextPage {

    @SerializedName("totalResults")
    private String totalResults;

    @SerializedName("searchTerms")
    private String searchTerm;

    @SerializedName("startIndex")
    private int startIndex;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
