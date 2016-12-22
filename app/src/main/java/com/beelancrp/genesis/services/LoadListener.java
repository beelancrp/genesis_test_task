package com.beelancrp.genesis.services;

import java.util.List;


public interface LoadListener {

    void dataLoaded(List result);

    void dataLoadFailed(Exception e);

    void noExistingData();
}
