package com.chakra.shoppinglist.fragments

import android.os.Bundle
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.model.ShoppingPlan
import com.chakra.shoppinglist.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.screen_add_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddProductFragment : BaseFragment(), ViewPager.OnPageChangeListener {
    companion object {
        private const val PARAM_SHOPPING_PLAN = "shopping_plan"

        fun getDataBundle(shoppingPlan: ShoppingPlan): Bundle {
            val args = Bundle()
            args.putSerializable(PARAM_SHOPPING_PLAN, shoppingPlan)
            return args
        }

        fun create(shoppingPlan: ShoppingPlan, category: Category): AddProductFragment {
            val fragment = AddProductFragment()
            fragment.arguments = getDataBundle(shoppingPlan)
            return fragment
        }
    }

    private var lastCategorySelected: String? = null
    private val viewModel: AddProductViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val shoppingPlan = arguments?.getSerializable(PARAM_SHOPPING_PLAN) as ShoppingPlan
            viewModel.shoppingPlan = shoppingPlan
        }
    }

    override fun getTitle(): String = getString(R.string.toolbar_title_add_product)

    override val resourceLayoutId: Int
        get() = R.layout.screen_add_product

    override fun getBaseViewModel() = viewModel

    override fun initialize() {
        pagerHeader.drawFullUnderline = false
        pagerHeader.tabIndicatorColor = requireContext().resources.getColor(R.color.primary)

        viewModel.categoryList.observe(viewLifecycleOwner) {
            it?.let {
                updateTabList(it)
            }
        }
    }

    override fun isFloatingButtonEnabled() = true

    override fun onFloatingButtonClicked() {
        findNavController().navigate(R.id.action_addProductScreen_to_createProductScreen,
                CreateProductFragment.getDataBundle(lastCategorySelected))
    }

    override fun onStart() {
        super.onStart()
        viewModel.reloadCategories()
    }

    private fun updateTabList(categories: List<Category?>) {
        val fragments = mutableListOf<ProductsFragment>()
        categories.forEach {
            it?.let {
                val fragment = ProductsFragment.create(viewModel.shoppingPlan, it)
                fragments.add(fragment)
            }
        }

        pager.removeOnPageChangeListener(this)
        pager.addOnPageChangeListener(this)
        pager.offscreenPageLimit = fragments.size
        val adapter = ProductsFragmentAdapter(parentFragmentManager, fragments)
        pager.adapter = adapter

        lastCategorySelected?.let {
            val position = categories.indexOf(Category(it))
            pager.currentItem = position
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        lastCategorySelected = currentTitle(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    private fun currentTitle(position: Int): String? {
        val adapter = pager.adapter as ProductsFragmentAdapter
        val fragment = adapter.getItem(position) as ProductsFragment
        return fragment.getTitle()
    }
}