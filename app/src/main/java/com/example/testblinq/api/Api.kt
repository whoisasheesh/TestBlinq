package com.example.testblinq.api

class Api {
    val authApi = "https://us-central1-blinkapp-684c1.cloudfunctions.net/fakeAuth"

    companion object {
        var instance: Api? = null
            get() {
                if (field == null) field = Api()
                return field
            }
            private set
    }
}