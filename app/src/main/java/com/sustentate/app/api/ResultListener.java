package com.sustentate.app.api;

/*
 * Created by mzorilla on 11/4/17.
 */

public interface ResultListener<T> {
    void loading();
    void finish(T result);
    void error(Throwable error);
}
