package com.chakra.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.utils.Analytics
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import com.chakra.shoppinglist.viewmodel.ProductsViewModel
import com.chakra.shoppinglist.views.Dialogs
import kotlinx.android.synthetic.main.fragment_view_category_products.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ProductsListFragment : BaseFragment() {

    companion object {
        private const val PARAM_CATEGORY = "category"
        private const val PARAM_SHOPPING_PLAN = "shopping_plan"

        fun getDataBundle(category: Category): Bundle {
            val args = Bundle()
            args.putSerializable(PARAM_CATEGORY, category)
            return args
        }

        @JvmStatic
        fun create(category: Category): ProductsListFragment {
            val fragment = ProductsListFragment()
            fragment.arguments = getDataBundle(category)
            return fragment
        }
    }

    private val viewModel: ProductsViewModel by viewModel()
    val commonViewModel: CommonViewModel by sharedViewModel()

    private lateinit var adapter: ProductsAdapter

    override val resourceLayoutId: Int
        get() = R.layout.fragment_view_category_products

    override fun getBaseViewModel() = viewModel

    override fun initialize() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        productList.layoutManager = layoutManager
        adapter = ProductsAdapter()
        productList.adapter = adapter

        viewModel.productsLiveData.observe(viewLifecycleOwner) { list: List<Product>? ->
            if (list.isNullOrEmpty()) {
                productList.visibility = View.GONE
                labelEmpty.visibility = View.VISIBLE
            } else {
                productList.visibility = View.VISIBLE
                labelEmpty.visibility = View.GONE
                adapter.setList(list)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        arguments?.let {
            viewModel.init(commonViewModel.shoppingPlanCartListData.value!!, it.getSerializable(PARAM_CATEGORY) as Category)
        }
    }

    override fun getTitle(): String = arguments?.let {
        (it.getSerializable(PARAM_CATEGORY) as Category).name
    } ?: ""

    fun onProductSelected(position: Int, product: Product) {
        if (commonViewModel.isProductAdded(product)) {
            viewModel.removeFromCart(product)
            commonViewModel.removeProductId(product.id)
        } else {
            viewModel.moveToCart(product)
            commonViewModel.addProductId(product.id)
            val analytics = Analytics(context)
            analytics.cartItemAdded(product)
        }
        adapter.notifyItemChanged(position)
    }

    fun onProductsOptions(product: Product) {
        val options = Arrays.asList(
                getString(R.string.button_update),
                getString(R.string.button_remove)
        )
        val dialogs = Dialogs(context)
        dialogs.options(product.name, options) { option: Int ->
            if (option == 0) {
                editProduct(product)
            } else if (option == 1) {
                confirmRemoveProduct(product)
            }
        }
    }

    private fun editProduct(product: Product) {
        findNavController().navigate(R.id.action_productsFragment_to_createProductScreen,
                CreateProductFragment.getDataBundle(product))
    }

    private fun confirmRemoveProduct(product: Product) {
        val dialogs = Dialogs(context)
        dialogs.confirmation(product.name, getString(R.string.dialog_remove_product)) {
            removeProduct(product)
        }
    }

    private fun removeProduct(product: Product) {
        viewModel.deleteProduct(product)
    }

    inner class ProductsAdapter : RecyclerView.Adapter<ViewHolder>() {
        private var imageLoader: RequestManager = Glide.with(context!!)

        private var productList: List<Product>? = null

        fun setList(list: List<Product>?) {
            this.productList = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_available, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(position, productList!![position], imageLoader)
        }

        override fun getItemCount() = productList?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.product_image)
        var name: TextView = view.findViewById(R.id.product_name)
        var options: View = view.findViewById(R.id.product_options)

        fun bind(position: Int, product: Product, imageLoader: RequestManager) {
            itemView.setOnClickListener {
                onProductSelected(position, product)
            }
            if (commonViewModel.isProductAdded(product)) {
                itemView.setBackgroundColor(context!!.resources.getColor(R.color.item_selected))
            } else {
                itemView.background = null
            }
            name.text = product.name
            imageLoader.load(product.image).into(image)
            options.setOnClickListener { v: View? -> onProductsOptions(product) }
        }
    }
}