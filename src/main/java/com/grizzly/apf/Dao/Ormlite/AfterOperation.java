package com.grizzly.apf.Dao.Ormlite;

/**
 * Created by Fco on 09-12-2014.
 */
public interface AfterOperation<T> {
    void doAfterDao(T result);
}