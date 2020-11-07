package com.chakra.shoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.screen_add_product.*

class AddProductFragment : BaseFragment(), ViewPager.OnPageChangeListener {
    private var lastCategorySelected: String? = null
    private lateinit var viewModel: AddProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(viewModelStore,
                ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
                .get(AddProductViewModel::class.java)
    }

    override fun getTitle(): String = getString(R.string.toolbar_title_add_product)

    override val resourceLayoutId: Int
        get() = R.layout.screen_add_product

    override fun initialize() {
        pagerHeader.drawFullUnderline = false
        pagerHeader.tabIndicatorColor = requireContext().resources.getColor(R.color.primary)

        productCreate.setOnClickListener {
            val intent: Intent = CreateProductActivity.intent(this, getCurrentCategory())
            startActivity(intent)
        }

        viewModel.categoryList.observe(viewLifecycleOwner, {
            it?.let {
                updateTabList(it)
            }
        })
    }

    private fun updateTabList(categories: List<Category?>) {
        val fragments = mutableListOf<ProductsFragment>()

        categories.forEach {
            it?.let {
                val fragment = ProductsFragment.create(it.name())
                fragments.add(fragment)
            }
        }

        pager.removeOnPageChangeListener(this)
        pager.addOnPageChangeListener(this)
        pager.offscreenPageLimit = fragments.size
        val adapter = ProductsFragmentAdapter(parentFragmentManager, fragments)
        pager.adapter = adapter

        lastCategorySelected?.let {
            val position = categories.indexOf(Category(lastCategorySelected))
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

    private fun getCurrentCategory(): String {}

    private fun currentTitle(position: Int): String? {
        val adapter = pager.adapter as ProductsFragmentAdapter
        val fragment = adapter.getItem(position) as ProductsFragment
        return fragment.title()
    }
}