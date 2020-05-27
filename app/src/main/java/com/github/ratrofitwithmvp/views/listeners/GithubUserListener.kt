package com.github.ratrofitwithmvp.views.listeners

import com.github.ratrofitwithmvp.model.data.UserData
import com.github.ratrofitwithmvp.model.data.UserRipoData

interface GithubUserListener {
    fun showProgress()
    fun hideProgress()
    fun onSuccess(data: List<UserRipoData>)
    fun onSuccess(data: UserData)
    fun onFailure(message: String)
}