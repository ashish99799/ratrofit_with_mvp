package com.github.ratrofitwithmvp.views.listeners

import com.github.ratrofitwithmvp.model.data.RowData

interface MainActivityListener {
    fun showProgress()
    fun hideProgress()
    fun onSuccess(data: ArrayList<RowData>)
    fun onFailure(message: String)
}