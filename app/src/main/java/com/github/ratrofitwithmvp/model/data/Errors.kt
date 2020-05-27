package com.github.ratrofitwithmvp.model.data

data class Errors(
    var resource: String? = "",
    var code: String? = "",
    var field: String? = ""
)