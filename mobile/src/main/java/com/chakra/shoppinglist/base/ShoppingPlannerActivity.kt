package com.chakra.shoppinglist.base

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.fragments.TopProductListFragment
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.activity_main_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ShoppingPlannerActivity : AppCompatActivity() {
    companion object {
        const val SHADOW_COLOR_TO_STATUS_BAR = "#0D000000"
    }
    private var currentFragment: BaseFragment? = null
    val commonViewModel: CommonViewModel by viewModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("chakra", "commonviewmodel: $commonViewModel")
        setContentView(R.layout.activity_main_layout)
        setSupportActionBar(toolbar)

        floatingButton.setOnClickListener {
            currentFragment?.onFloatingButtonClicked()
        }

        commonViewModel.shoppingPlanCartListData.observe(this) { shoppingPlan ->
            if (shoppingPlan == null) {
                backdrop.visibility = View.GONE
                return@observe
            }
            shoppingPlan.planType?.image.let {
                backdrop.visibility = View.VISIBLE
                Glide.with(this).load(it).into(backdrop)
            }
        }

        commonViewModel.color.observe(this) { color ->
            color?.let {
                supportActionBar?.apply {
                    val colorDrawable = ColorDrawable(Color.parseColor(it))
                    colorDrawable.setColorFilter(Color.parseColor(SHADOW_COLOR_TO_STATUS_BAR), PorterDuff.Mode.SRC_ATOP);
                    collapsing_layout.statusBarScrim = colorDrawable

                    collapsing_layout.setContentScrimColor(Color.parseColor(it))
                    //collapsing_layout.setExpandedTitleColor(Color.parseColor(it))
                    collapsing_layout.setBackgroundColor(Color.parseColor(it))

                    floatingButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor(it))
                }
            }
        }
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentStarted(fm: FragmentManager, fragment: Fragment) {
                super.onFragmentStarted(fm, fragment)
                if (fragment !is TopProductListFragment && fragment is BaseFragment) {
                    updateTitle(fragment)
                    updateBackIcon(fragment)
                    updateFloatingButton(fragment)
                }
                Log.e("chakra", "onFragmentStarted() = $fragment")
            }
        }, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    open fun updateBackIcon(fragment: BaseFragment) {
        supportActionBar?.setDisplayHomeAsUpEnabled(fragment.enableBack())
    }

    open fun updateTitle(fragment: BaseFragment) {
        supportActionBar?.title = fragment.getTitle()
        title = fragment.getTitle()
        collapsing_layout.title = fragment.getTitle()
    }

    fun updateFloatingButton(fragment: BaseFragment) {
        currentFragment = fragment
        currentFragment?.let {
            floatingButton.visibility = if (it.isFloatingButtonEnabled()) View.VISIBLE else View.GONE
        }
    }

    private fun toast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }
}