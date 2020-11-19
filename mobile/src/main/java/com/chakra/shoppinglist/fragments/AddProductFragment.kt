package com.chakra.shoppinglist.fragments

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.viewmodel.AddProductViewModel
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.screen_add_product.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddProductFragment : BaseFragment() {
    private var lastCategorySelectedIndex: Int = 0
    private val viewModel: AddProductViewModel by viewModel()
    private val commonViewModel: CommonViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("chakra", "$viewModel  = $commonViewModel  - ${commonViewModel.shoppingPlanCartListData.value}")
        viewModel.shoppingPlan = commonViewModel.shoppingPlanCartListData.value!!
    }

    override fun getTitle(): String = getString(R.string.toolbar_title_add_product)

    override val resourceLayoutId: Int
        get() = R.layout.screen_add_product

    override fun getBaseViewModel() = viewModel

    override fun initialize() {
        /*pagerHeader.drawFullUnderline = false
        pagerHeader.tabIndicatorColor = requireContext().resources.getColor(R.color.primary)*/

        viewModel.categoryList.observe(viewLifecycleOwner) {
            it?.let {
                updateTabList(it)
            }
        }
    }

    override fun isFloatingButtonEnabled() = true

    override fun onFloatingButtonClicked() {
        findNavController().navigate(R.id.action_addProductScreen_to_createProductScreen,
                CreateProductFragment.getDataBundle(viewModel.categoryList.value?.get(lastCategorySelectedIndex)))
    }

    override fun onStart() {
        super.onStart()
        viewModel.reloadCategories()
    }

    val callBack = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            lastCategorySelectedIndex = position
        }

    }

    private fun updateTabList(categories: List<Category>) {
        /*pager.removeOnPageChangeListener(this)
        pager.addOnPageChangeListener(this)
        pager.offscreenPageLimit = fragments.size*/
        pager.offscreenPageLimit = OFFSCREEN_PAGE_LIMIT_DEFAULT
        pager.unregisterOnPageChangeCallback(callBack)
        pager.registerOnPageChangeCallback(callBack)
        val adapter = ProductsFragmentAdapter(this, categories)
        pager.adapter = adapter

        pager.currentItem = lastCategorySelectedIndex
        pagerHeader.tabMode = MODE_SCROLLABLE
        TabLayoutMediator(pagerHeader, pager) { tab, position ->
            tab.text = categories[position].name
        }.attach()
    }
}