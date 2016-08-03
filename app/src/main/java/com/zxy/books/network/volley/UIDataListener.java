package com.zxy.books.network.volley;

public interface UIDataListener<T> {
    public void onDataChanged(T data);

    public void onErrorHappened(String errorMessage);
}