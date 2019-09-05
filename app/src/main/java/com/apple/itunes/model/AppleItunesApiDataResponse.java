package com.apple.itunes.model;

import java.util.ArrayList;

public class AppleItunesApiDataResponse {
    Integer resultCount;

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public ArrayList<SongListModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<SongListModel> results) {
        this.results = results;
    }

    ArrayList<SongListModel> results = new ArrayList<>();




}
