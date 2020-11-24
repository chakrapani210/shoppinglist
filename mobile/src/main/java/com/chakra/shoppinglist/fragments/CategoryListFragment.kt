package com.chakra.shoppinglist.fragments

import android.os.Bundle
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.viewmodel.CategoriesViewModel
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.fragment_category_list_layout.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryListFragment: BaseFragment() {
    private val viewModel: CategoriesViewModel by viewModel()
    private val commonViewModel: CommonViewModel by sharedViewModel()
    private val glide: RequestManager = get()
    private lateinit var adapter: CategoryAdapter
    override fun getBaseViewModel() = viewModel

    override fun getTitle() = ""

    override val resourceLayoutId: Int
        get() = R.layout.fragment_category_list_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            commonViewModel.shoppingPlanCartListData.value?.let {
                viewModel.init(it)
            }
        }
    }

    override fun initialize() {
        super.initialize()
        adapter = CategoryAdapter(requireContext(), glide, object : ChildClickCallback {
            override fun onChildSelected(product: Product) {
                if (commonViewModel.isProductAdded(product)) {
                    viewModel.removeFromCart(product)
                } else {
                    viewModel.moveToCart(product)
                }
            }

            override fun onChildOptionsSelected(product: Product) {
                // TODO: handle Product options
            }
        })

        expandableList.setAdapter(adapter)
        expandableList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(expandableList.getContext(), LinearLayoutManager.VERTICAL)
        expandableList.addItemDecoration(dividerItemDecoration)
        viewModel.categoriesWithProductsLiveData.observe(this) {
            adapter.setData(it)
        }
    }
}

