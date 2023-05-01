package com.dbclass.hanstagram.data

import androidx.lifecycle.MutableLiveData

class ListLiveData<T> : MutableLiveData<MutableList<T>>() {
    private val list = mutableListOf<T>()

    init {
        value = list
    }

    fun add(item: T) {
        list.add(item)
        value = list
    }
    fun addAll(items: List<T>){
        list.addAll(items)
        value = list
    }
    fun remove(item: T) {
        list.remove(item)
        value = list
    }

    fun clear() {
        list.clear()
        value = list
    }
}