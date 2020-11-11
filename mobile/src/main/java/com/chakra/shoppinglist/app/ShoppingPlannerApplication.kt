package com.chakra.shoppinglist.app

import com.chakra.shoppinglist.di.shoppingPlannerModule
import com.orm.SugarApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ShoppingPlannerApplication : SugarApp() {
    override fun onCreate() {
        super.onCreate()

        //Fabric.with(this, new Crashlytics());
        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger(Level.ERROR)

            // use the Android context given there
            androidContext(this@ShoppingPlannerApplication)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            modules(shoppingPlannerModule)
        }
    }
}