package com.beelancrp.genesis.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchItem implements Serializable {

    public static final long serialVersionUID = 1L;

    @SerializedName("title")
    private String itemTitle;

    @SerializedName("link")
    private String itemLink;


    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }
}
