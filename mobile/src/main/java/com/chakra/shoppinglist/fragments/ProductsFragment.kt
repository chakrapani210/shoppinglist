package com.chakra.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.utils.Analytics
import com.chakra.shoppinglist.viewmodel.ProductsViewModel
import com.chakra.shoppinglist.views.Dialogs
import kotlinx.android.synthetic.main.fragment_planner_list_layout.*
import java.util.*

class ProductsFragment : BaseFragment() {

    companion object {
        private const val PARAM_CATEGORY = "category"
        fun getDataBundle(category: String?): Bundle {
            val args = Bundle()
            args.putString(PARAM_CATEGORY, category)
            return args
        }

        fun create(category: String?): ProductsFragment {
            val fragment = ProductsFragment()
            fragment.arguments = getDataBundle(category)
            return fragment
        }
    }

    private lateinit var viewModel: ProductsViewModel
    private lateinit var adapter: PlanListAdapter

    override val resourceLayoutId: Int
        get() = R.layout.fragment_view_category_products

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(viewModelStore,
                ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
                .get(ProductsViewModel::class.java)
    }

    override fun initialize() {
        val layoutManager = GridLayoutManager(context, 2)
        planList.layoutManager = layoutManager
        adapter = PlanListAdapter()
        planList.adapter = adapter

        viewModel.reloadProducts(arguments?.getString(PARAM_CATEGORY, "") ?: "")
        viewModel.productsLiveData.observe(viewLifecycleOwner, { list: List<Product>? ->
            list?.let {
                adapter.setList(it)
            }
        })
    }

    override fun getTitle(): String = arguments?.getString(PARAM_CATEGORY, "") ?: ""

    fun title(): String {
        return arguments?.getString(PARAM_CATEGORY, "") ?: ""
    }

    fun onProductSelected(product: Product?) {
        viewModel.moveToCart(product!!)
        val analytics = Analytics(context)
        analytics.cartItemAdded(product)
    }

    fun onProductsOptions(product: Product) {
        val options = Arrays.asList(
                getString(R.string.button_update),
                getString(R.string.button_remove)
        )
        val dialogs = Dialogs(context)
        dialogs.options(product.name(), options) { option: Int ->
            if (option == 0) {
                editProduct(product)
            } else if (option == 1) {
                confirmRemoveProduct(product)
            }
        }
    }

    private fun editProduct(product: Product) {
        val intent = CreateProductActivity.intent(context, product)
        startActivity(intent)
    }

    private fun confirmRemoveProduct(product: Product) {
        val dialogs = Dialogs(context)
        dialogs.confirmation(product.name(), getString(R.string.dialog_remove_product)) { removeProduct(product) }
    }

    private fun removeProduct(product: Product) {
        viewModel.deleteProduct(product)
    }

    inner class PlanListAdapter : RecyclerView.Adapter<ViewHolder>() {
        private var imageLoader: RequestManager = Glide.with(context!!)

        private var prodcutList: List<Product>? = null

        fun setList(list: List<Product>?) {
            this.prodcutList = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_available, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(prodcutList!![position], imageLoader)
        }

        override fun getItemCount() = prodcutList?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView? = view.findViewById(R.id.product_image)
        var name: TextView? = view.findViewById(R.id.product_name)
        var options: View? = view.findViewById(R.id.product_options)

        fun bind(product: Product, imageLoader: RequestManager) {
            name!!.text = product.name()
            imageLoader.load(product.image()).into(image!!)
            options!!.setOnClickListener { v: View? -> onProductsOptions(product) }
        }
    }
}