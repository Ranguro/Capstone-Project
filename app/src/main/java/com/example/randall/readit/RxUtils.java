package com.example.randall.readit;

import rx.Subscription;

/**
 * Created by Randall on 12/06/2016.
 */
public class RxUtils {

    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
