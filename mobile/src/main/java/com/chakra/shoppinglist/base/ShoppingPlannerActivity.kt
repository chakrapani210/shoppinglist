package com.chakra.shoppinglist.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.tasks.migration.Migration
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.activity_main_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShoppingPlannerActivity : AppCompatActivity() {
    companion object {
        private const val FIELD_MIGRATION_DONE = "migration.done"
    }

    private var currentFragment: BaseFragment? = null
    val commonViewModel: CommonViewModel by viewModel()

    private fun isMigrationDone() = getPreferences(Activity.MODE_PRIVATE).getBoolean(FIELD_MIGRATION_DONE, false)
    private fun setMigrationDone() = getPreferences(Activity.MODE_PRIVATE).edit().putBoolean(FIELD_MIGRATION_DONE, true).commit()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isMigrationDone()) {
            val migration = Migration(this, null)
            migration.execute()
            setMigrationDone()
        }

        setContentView(R.layout.activity_main_layout)
        setSupportActionBar(findViewById(R.id.toolbar))

        floatingButton.setOnClickListener {
            currentFragment?.onFloatingButtonClicked()
        }
    }

    fun updateFloatingButton(fragment: BaseFragment) {
        currentFragment = fragment
        currentFragment?.let {
            floatingButton.visibility = if (it.isFloatingButtonEnabled()) View.VISIBLE else View.GONE
        }
    }

    protected fun toast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }
}