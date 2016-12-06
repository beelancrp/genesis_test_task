package com.beelancrp.genesis.db;

import com.beelancrp.genesis.data.SearchItem;

import java.util.List;

/**
 * Created by bilan on 06.12.2016.
 */

public class DataManager {
    private static final String DEBUG_TAG = "DataManager";

    private DataBaseHelper dbHelper;

    public DataManager() {
        dbHelper = DataBaseHelper.getDatabaseHelper();
    }

    public void saveResults(final List<SearchItem> resultList) {
        for (SearchItem result : resultList) {
            dbHelper.addResult(result);
        }
    }

    public List<Integer> getResultsIDList() {
        return dbHelper.getResultsIDList();
    }

    public List<Integer> getResultsIDList(int limit) {
        return dbHelper.getResultsIDList(limit);
    }

    public SearchItem getResult(int resultID) {
        return dbHelper.getResult(resultID);
    }

    public List<SearchItem> getResults() {
        return dbHelper.getAllResults();
    }

    public void saveTerm(String term) {
        dbHelper.addTerm(term);
    }

    public String getTerm(int termID) {
        return dbHelper.getTerm(termID);
    }

    public void deleteAlTerms() {
        dbHelper.clearTermTable();
    }

    public void deleteAllResults() {
        dbHelper.clearResultTable();
    }

    public void deleteAllData() {
        dbHelper.clearAllTables();
    }

}