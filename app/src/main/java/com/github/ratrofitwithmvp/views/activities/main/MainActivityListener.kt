package com.github.ratrofitwithmvp.views.activities.main

import com.github.ratrofitwithmvp.model.RowData

interface MainActivityListener {
    fun showProgress()
    fun hideProgress()
    fun onSuccess(data: ArrayList<RowData>)
    fun onFailure(message: String)
}