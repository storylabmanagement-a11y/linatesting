package com.explorefaraya.app

import android.app.Application
import com.explorefaraya.app.data.model.ExploreCatalog
import com.google.firebase.FirebaseApp

class ExploreFarayaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        ExploreCatalog.init(this)
    }
}
