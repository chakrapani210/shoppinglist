package com.chakra.shoppinglist.fragments

import android.content.Intent
import android.graphics.Paint
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
import com.chakra.shoppinglist.model.CartWithProduct
import com.chakra.shoppinglist.utils.Analytics
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import com.chakra.shoppinglist.viewmodel.ShoppingPlanViewModel
import kotlinx.android.synthetic.main.screen_cart.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShoppingCartViewFragment : BaseFragment() {
    private val viewModel: ShoppingPlanViewModel by viewModel()
    private val commonViewModel: CommonViewModel by sharedViewModel()

    override val resourceLayoutId: Int
        get() = R.layout.screen_cart

    private lateinit var adapter: ShoppingPlanAdapter

    override fun getBaseViewModel() = viewModel

    override fun initialize() {
        val analytics = Analytics(requireContext())
        analytics.appLaunched()

        val layoutManager = LinearLayoutManager(context)
        product_list.layoutManager = layoutManager
        adapter = ShoppingPlanAdapter()
        product_list.adapter = adapter

        commonViewModel.shoppingPlanFullData.observe(viewLifecycleOwner) { planWIthCart ->
            viewModel.init(commonViewModel.shoppingPlanCartListData.value!!)
            updateList(planWIthCart?.cart)
        }

    }

    override fun onFloatingButtonClicked() {
        onAddProduct()
    }

    override fun isFloatingButtonEnabled() = true

    override fun getTitle() = getString(R.string.toolbar_title_main)

    fun onProductSelected(product: CartWithProduct?) {
        product?.let {
            viewModel.toggleSelection(it)
        }
    }

    fun onShare(products: List<CartWithProduct>?) {
        products?.let {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, shareContent(it))
            intent.type = "text/plain"

            startActivity(intent)
        }
    }

    private fun onAddProduct() {
        findNavController().navigate(R.id.action_shoppingCartViewScreen_to_addProductScreen)
    }

    private fun shareContent(products: List<CartWithProduct>): String? {
        val result = StringBuilder()
        var lastCategory = ""
        /*for (product in products) {
            if (!product.inCartProductData.selected) {
                if (!TextUtils.equals(product.category(), lastCategory)) {
                    lastCategory = product.category()
                    if (result.length != 0) {
                        result.append("\n")
                    }
                    result.append(lastCategory).append(":\n")
                }
                result.append("   - ").append(product.name()).append("\n")
            }
        }*/
        return result.toString()
    }


    private fun updateList(products: List<CartWithProduct>?) {
        products?.let {
            if (it.isEmpty()) {
                //disableToolbarAction()
                label_empty.visibility = View.VISIBLE
                product_list.visibility = View.GONE
            } else {
                label_empty.visibility = View.GONE
                product_list.visibility = View.VISIBLE
                adapter.updateList(products)
                // enableToolbarAction(R.drawable.ic_share) { v -> observer.onShare(products) }
            }
        }

    }

    inner class ShoppingPlanAdapter : RecyclerView.Adapter<ViewHolder>() {
        private var productsList: List<CartWithProduct>? = null
        private var imageLoader = Glide.with(context!!)

        fun updateList(list: List<CartWithProduct>?) {
            this.productsList = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_in_cart, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(productsList!![position], imageLoader)
        }

        override fun getItemCount() = productsList?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var row: View = view.findViewById(R.id.product_row)
        var image: ImageView = view.findViewById(R.id.product_image)
        var name: TextView = view.findViewById(R.id.product_name)
        var check: ImageView = view.findViewById(R.id.product_check)

        fun bind(product: CartWithProduct, imageLoader: RequestManager) {
            itemView.setOnClickListener {
                onProductSelected(product)
            }
            if (product.cart.selected) {
                row.setBackgroundColor(context!!.resources.getColor(R.color.item_selected))
                name.paintFlags = (name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                check.visibility = View.VISIBLE
            } else {
                row.background = null
                name.paintFlags = (name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                check.visibility = View.GONE
            }

            name.text = product.product.name

            imageLoader.load(product.product.image).into(image)
        }
    }
}