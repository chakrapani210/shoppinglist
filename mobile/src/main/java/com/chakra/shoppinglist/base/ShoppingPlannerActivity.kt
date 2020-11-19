package com.chakra.shoppinglist.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.activity_main_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ShoppingPlannerActivity : AppCompatActivity() {
    private var currentFragment: BaseFragment? = null
    val commonViewModel: CommonViewModel by viewModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("chakra", "commonviewmodel: $commonViewModel")
        setContentView(R.layout.activity_main_layout)
        setSupportActionBar(findViewById(R.id.toolbar))

        floatingButton.setOnClickListener {
            currentFragment?.onFloatingButtonClicked()
        }

        commonViewModel.color.observe(this) { color ->
            color?.let {
                supportActionBar?.apply {
                    val colorDrawable = ColorDrawable(Color.parseColor(it))
                    setBackgroundDrawable(colorDrawable)
                }
            }
        }
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                super.onFragmentAttached(fm, f, context)
                Log.e("chakra", "onFragmentAttached() = $f")
            }

            override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                super.onFragmentCreated(fm, f, savedInstanceState)
                Log.e("chakra", "onFragmentCreated() = $f")
            }

            override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                super.onFragmentStarted(fm, f)
                Log.e("chakra", "onFragmentStarted() = $f")
            }

            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                super.onFragmentResumed(fm, f)
                Log.e("chakra", "onFragmentResumed() = $f")
            }

            override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                super.onFragmentStopped(fm, f)
                Log.e("chakra", "onFragmentStopped() = $f")
            }

            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentDestroyed(fm, f)
                Log.e("chakra", "onFragmentDestroyed() = $f")
            }

            override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                super.onFragmentDetached(fm, f)
                Log.e("chakra", "onFragmentDetached() = $f")
            }
        }, true)
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