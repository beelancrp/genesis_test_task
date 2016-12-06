package com.beelancrp.genesis.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beeLAN on 03.12.2016.
 */

public class Queries {

    @SerializedName("nextPage")
    private List<NextPage> nextPage;

    public Queries() {
        this.nextPage = new ArrayList<>();
    }

    public List<NextPage> getNextPage() {
        return nextPage;
    }

    public void setNextPage(List<NextPage> nextPage) {


        this.nextPage = nextPage;
    }
}
