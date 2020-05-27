package com.github.ratrofitwithmvp.presenters

import android.content.Context
import com.github.ratrofitwithmvp.model.api.ApiClient
import com.github.ratrofitwithmvp.model.data.UserData
import com.github.ratrofitwithmvp.model.data.UserRipoData
import com.github.ratrofitwithmvp.utils.CheckInternetConnectionAvailable
import com.github.ratrofitwithmvp.views.listeners.GithubUserListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GithubUserPresenter(val githubUserListener: GithubUserListener) {

    // Over Activity Listener
    private var myCompositeDisposable: CompositeDisposable? = null

    fun onUserInfo(context: Context, query: String) {
        // Check Internet connectivity
        if (context.CheckInternetConnectionAvailable()) {
            // Ratrofit API Calling
            myCompositeDisposable = CompositeDisposable()
            myCompositeDisposable?.add(
                ApiClient()
                    .getUserInfo(query)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })
            )
        } else {
            // Internet is not connected
            githubUserListener.onFailure("Please check your internet connection!")
        }
    }

    private fun onResponse(response: UserData) {
        githubUserListener.hideProgress()
        githubUserListener.onSuccess(response)
    }

    private fun onResponseList(response: List<UserRipoData>) {
        githubUserListener.hideProgress()
        githubUserListener.onSuccess(response)
    }

    private fun onFailure(error: Throwable) {
        githubUserListener.hideProgress()
        githubUserListener.onFailure("Fail ${error.message}")
    }

    fun onUserRipo(context: Context, query: String) {
        // Check Internet connectivity
        if (context.CheckInternetConnectionAvailable()) {
            // API Calling Start
            githubUserListener.showProgress()

            // Ratrofit API Calling
            myCompositeDisposable = CompositeDisposable()
            myCompositeDisposable?.add(
                ApiClient()
                    .getUserRipo(query)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ response -> onResponseList(response) }, { t -> onFailure(t) })
            )
        } else {
            // Internet is not connected
            githubUserListener.hideProgress()
            githubUserListener.onFailure("Please check your internet connection!")
        }
    }
}