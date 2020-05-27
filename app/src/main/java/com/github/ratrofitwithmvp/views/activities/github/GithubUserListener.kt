package com.github.ratrofitwithmvp.views.activities.github

import com.github.ratrofitwithmvp.model.UserData
import com.github.ratrofitwithmvp.model.UserRipoData

interface GithubUserListener {
    fun showProgress()
    fun hideProgress()
    fun onSuccess(data: List<UserRipoData>)
    fun onSuccess(data: UserData)
    fun onFailure(message: String)
}