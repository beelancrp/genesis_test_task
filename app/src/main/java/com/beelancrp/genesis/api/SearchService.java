package com.beelancrp.genesis.api;

import com.beelancrp.genesis.data.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by beeLAN on 03.12.2016.
 */

public interface SearchService {

    @GET("customsearch/v1")
    Call<SearchResponse> getSearchResponse(@Query("q") String terms,
                                           @Query("cx") String searchEngineCode,
                                           @Query("key") String apiKey,
                                           @Query("start") int startPosition,
                                           @Query("num") int offset);
}
