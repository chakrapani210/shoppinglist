package com.chakra.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.viewmodel.ManageCategoriesViewModel
import com.chakra.shoppinglist.views.Dialogs
import com.chakra.shoppinglist.views.Dialogs.OnInputConfirmed
import kotlinx.android.synthetic.main.screen_manage_categories.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ManageCategoriesFragment : BaseFragment() {
    private lateinit var adapter: CategoriesAdapter
    private val viewModel: ManageCategoriesViewModel by viewModel()

    override fun getTitle() = getString(R.string.toolbar_title_manage_categories)

    override val resourceLayoutId = R.layout.screen_manage_categories

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.reloadCategories()
        }
    }

    override fun getBaseViewModel() = viewModel

    override fun initialize() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        categoryList.layoutManager = layoutManager
        adapter = CategoriesAdapter()
        categoryList.adapter = adapter

        viewModel.categoryList.observe(viewLifecycleOwner) {
            updateList(it)
        }
    }

    private fun updateList(categories: List<Category>?) {
        if (categories.isNullOrEmpty()) {
            labelEmpty.visibility = View.VISIBLE
            categoryList.visibility = View.GONE
        } else {
            labelEmpty.visibility = View.GONE
            categoryList.visibility = View.VISIBLE
            adapter.setList(categories)
        }
    }

    inner class CategoriesAdapter : RecyclerView.Adapter<ViewHolder>() {
        private var categories: List<Category>? = null

        fun setList(list: List<Category>?) {
            this.categories = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(categories!![position])
        }

        override fun getItemCount() = categories?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView? = view.findViewById(R.id.categoryName)

        fun bind(category: Category) {
            name!!.text = category.name()

            itemView.setOnClickListener { v: View? ->
                onCategorySelected(category)
            }
        }
    }

    fun onCategorySelected(category: Category) {
        val options = Arrays.asList(
                getString(R.string.button_rename),
                getString(R.string.button_remove)
        )

        val dialogs = Dialogs(requireContext())
        dialogs.options(category.name(), options) { option: Int ->
            if (option == 0) {
                requestCategoryName(category)
            } else if (option == 1) {
                confirmRemoveCategory(category)
            }
        }
    }

    private fun requestCategoryName(category: Category) {
        val dialogs = Dialogs(requireContext())
        dialogs.input(requireContext(), getString(R.string.label_product_edit_category),
                category.name(), OnInputConfirmed { input: String ->
            viewModel.renameCategory(category, input)
        })
    }

    private fun confirmRemoveCategory(category: Category) {
        val dialogs = Dialogs(requireContext())
        dialogs.confirmation(category.name(), getString(R.string.dialog_remove_category)) {
            viewModel.deleteCategory(category)
        }
    }
}