package com.b18dccn562.phonemanager.service

object ObserverForService {

    private val listObserver = mutableListOf<Observer>()

    fun update() {
        for (observer in listObserver) {
            observer.update()
        }
    }

    fun addObserver(observer: Observer) {
        if (!listObserver.contains(observer)) {
            listObserver.add(observer)
        }
    }

    fun removeObserver(observer: Observer) {
        if (listObserver.contains(observer)) {
            listObserver.remove(observer)
        }
    }

}