package com.appachhi.sdk;

import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private static EventBus instance;
    private String action;
    private List<Listener> listenerList = new ArrayList<>();

    private EventBus() {
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    void post(String action) {
        this.action = action;
        if (!listenerList.isEmpty() && action != null) {
            for (Listener listener : listenerList) {
                listener.onChange(action);
            }
        }
    }

    public void register(Listener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
            if (action != null) {
                listener.onChange(action);
            }
        }
    }

    public void unRegister(Listener listener) {
        listenerList.remove(listener);
    }

    interface Listener {
        void onChange(String action);
    }
}
