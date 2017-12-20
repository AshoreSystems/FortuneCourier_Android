package com.example.aspl.fortunecourier.utility;

import java.util.Observable;

/**
 * Created by aspl on 13/12/17.
 */

public class UpdateDrawerObeserver extends Observable {

    private static UpdateDrawerObeserver instance = new UpdateDrawerObeserver();

    public static UpdateDrawerObeserver getInstance() {
        return instance;
    }

    private UpdateDrawerObeserver() {

    }

    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }
}

