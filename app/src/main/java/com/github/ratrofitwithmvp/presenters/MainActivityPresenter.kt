package com.github.ratrofitwithmvp.presenters

import android.content.Context
import com.github.ratrofitwithmvp.model.api.ApiClient
import com.github.ratrofitwithmvp.model.data.DataResponse
import com.github.ratrofitwithmvp.model.data.RowData
import com.github.ratrofitwithmvp.utils.CheckInternetConnectionAvailable
import com.github.ratrofitwithmvp.views.listeners.MainActivityListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivityPresenter(val mainActivityListener: MainActivityListener) {

    private var myCompositeDisposable: CompositeDisposable? = null

    fun getSearchUser(context: Context, query: String, page: Int) {
        // Check Internet connectivity
        if (context.CheckInternetConnectionAvailable()) {
            // API Calling Start
            mainActivityListener.showProgress()

            // Ratrofit API Calling
            myCompositeDisposable = CompositeDisposable()
            myCompositeDisposable?.add(
                ApiClient().getUserSearch(query, page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })
            )
        } else {
            // Internet is not connected
            mainActivityListener.hideProgress()
            mainActivityListener.onFailure("Please check your internet connection!")
        }
    }

    private fun onResponse(response: DataResponse) {
        mainActivityListener.hideProgress()
        mainActivityListener.onSuccess(response.items!! as ArrayList<RowData>)
    }

    private fun onFailure(error: Throwable) {
        mainActivityListener.hideProgress()
        mainActivityListener.onFailure("Fail ${error.message}")
    }
}