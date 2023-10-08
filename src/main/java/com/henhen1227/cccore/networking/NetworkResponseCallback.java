package com.henhen1227.cccore.networking;

import org.apache.http.HttpResponse;

public interface NetworkResponseCallback {
    void networkResponse(HttpResponse response, String error);
}
