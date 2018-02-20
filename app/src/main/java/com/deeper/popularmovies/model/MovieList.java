package com.deeper.popularmovies.model;

import java.util.ArrayList;

/**
 * Created by justo on 17/02/18.
 */

public class MovieList {

    private int page;
    private ArrayList<Movie> result;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Movie> getResult() {
        return result;
    }

    public void setResult(ArrayList<Movie> result) {
        this.result = result;
    }
}
