package com.chakra.shoppinglist.fragments

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.model.Category

class ProductsFragmentAdapter(val fragment: Fragment, val showRecents: Boolean) : FragmentStateAdapter(fragment!!) {
    override fun createFragment(position: Int): Fragment {
        return if (showRecents) {
            when (position) {
                0 -> RecentProductListFragment()
                1 -> TopProductListFragment()
                2 -> CategoryListFragment()
                else -> CategoryListFragment()
            }
        } else {
            when (position) {
                0 -> TopProductListFragment()
                1 -> CategoryListFragment()
                else -> CategoryListFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return if (showRecents) 3 else 2
    }

    fun getTitle(position: Int): String {
        return if (showRecents) {
            when (position) {
                0 -> getString(R.string.recent_products_label)
                1 -> getString(R.string.top_products_label)
                2 -> getString(R.string.categories_label)
                else -> getString(R.string.categories_label)
            }
        } else {
            when (position) {
                0 -> getString(R.string.top_products_label)
                1 -> getString(R.string.categories_label)
                else -> getString(R.string.categories_label)
            }
        }
    }

    fun getString(@StringRes resId: Int): String = fragment.getString(resId)
}