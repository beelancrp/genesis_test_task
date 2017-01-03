package com.beelancrp.genesis.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchItem implements Serializable {

    @SerializedName("title")
    private String itemTitle;

    @SerializedName("link")
    private String itemLink;

    public SearchItem() {

    }

    public SearchItem(String itemTitle, String itemLink) {
        this.itemTitle = itemTitle;
        this.itemLink = itemLink;
    }


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchItem that = (SearchItem) o;

        if (itemTitle != null ? !itemTitle.equals(that.itemTitle) : that.itemTitle != null)
            return false;
        return itemLink != null ? itemLink.equals(that.itemLink) : that.itemLink == null;

    }

    @Override
    public int hashCode() {
        int result = itemTitle != null ? itemTitle.hashCode() : 0;
        result = 31 * result + (itemLink != null ? itemLink.hashCode() : 0);
        return result;
    }
}
