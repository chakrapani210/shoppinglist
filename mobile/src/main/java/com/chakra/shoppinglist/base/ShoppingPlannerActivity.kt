package com.chakra.shoppinglist.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class ShoppingPlannerActivity : AppCompatActivity() {
    val commonViewModel: CommonViewModel by viewModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_layout)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    protected fun toast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }
}