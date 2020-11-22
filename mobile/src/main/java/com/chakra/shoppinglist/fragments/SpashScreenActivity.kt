package com.chakra.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.ShoppingPlannerActivity
import com.chakra.shoppinglist.database.AppDatabase
import com.chakra.shoppinglist.tasks.migration.Migration
import kotlinx.android.synthetic.main.activity_splash_screen_layout.*

class SplashScreenActivity : AppCompatActivity(), Migration.OnMigrationDone {
    private fun isMigrationDone() = getSharedPreferences(AppDatabase.DATABASE_INIT_PREF, Activity.MODE_PRIVATE)
            .getBoolean(AppDatabase.FIELD_MIGRATION_DONE, false)
    private val handler = Handler(Looper.getMainLooper())

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        handler.postDelayed({ migrate() }, 500)
        setContentView(R.layout.activity_splash_screen_layout)
    }

    private fun migrate() {
        if (!isMigrationDone()) {
            progress.visibility = View.VISIBLE
            val migration = Migration(this, this)
            migration.execute()
        } else {
            onMigrationDone()
        }
    }

    override fun onMigrationDone() {
        startActivity(Intent(this, ShoppingPlannerActivity::class.java))
        finish()
    }
}