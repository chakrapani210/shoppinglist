package com.chakra.shoppinglist.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.model.CategoryWithProducts
import com.chakra.shoppinglist.model.Product

class CategoryAdapter(val context: Context, val glide: RequestManager,
                      val childClickCallback: ChildClickCallback? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val ITEM_TYPE = 0
        const val GROUP_TYPE = 1
    }

    private var categoryData: List<CategoryWithProducts> = emptyList()
    private var expandedGroupIndex = -1

    fun setData(list: List<CategoryWithProducts>?) {
        list?.let {
            this.categoryData = list
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == GROUP_TYPE) getGroupView(parent) else getChildView(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (GROUP_TYPE == getItemViewType(position)) {
            bindGroupView(holder, getGroupIndex(position, expandedGroupIndex), expandedGroupIndex == getGroupIndex(position, expandedGroupIndex))
        } else {
            bindChildView(holder, expandedGroupIndex, getChildIndex(position, expandedGroupIndex))
        }
    }

    private fun getChildIndex(position: Int, expandedGroupIndex: Int): Int {
        return position - expandedGroupIndex -1
    }

    private fun getGroupIndex(position: Int, expandedGroupIndex: Int): Int {
        return if (expandedGroupIndex == -1 || position <= expandedGroupIndex) {
            position
        } else {
            position - getChildrenCount(expandedGroupIndex)
        }
    }

    override fun getItemCount(): Int {
        return if (expandedGroupIndex == -1) {
            categoryData.size
        } else {
            categoryData.size + getChildrenCount(expandedGroupIndex)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (expandedGroupIndex == -1
                || position <= expandedGroupIndex
                || position > expandedGroupIndex + getChildrenCount(expandedGroupIndex)) {
            GROUP_TYPE
        } else {
            ITEM_TYPE
        }
    }

    private fun getChildrenCount(groupPosition: Int) = categoryData[groupPosition].products?.size ?: 0

    private fun getGroup(groupPosition: Int) = categoryData[groupPosition]

    private fun getChild(groupPosition: Int, childPosition: Int) = categoryData[groupPosition].products?.get(childPosition)

    private fun getGroupView(parent: ViewGroup?): RecyclerView.ViewHolder {
       return GroupViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item_view, parent, false))
    }

    private fun getChildView(parent: ViewGroup?): RecyclerView.ViewHolder {
        return  ChildViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_category_product_item_view, parent, false))
    }

    private fun bindChildView(holder: RecyclerView.ViewHolder, groupPosition: Int, childPosition: Int) {
        (holder as ChildViewHolder).bind(getChild(groupPosition, childPosition))
    }

    private fun bindGroupView(holder: RecyclerView.ViewHolder, groupPosition: Int, expanded: Boolean) {
        if (holder is GroupViewHolder) {
            holder.bind(getGroup(groupPosition), groupPosition)
        }
    }

    inner class ChildViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.productName)
        val image: ImageView = itemView.findViewById(R.id.productImage)
        val options: ImageView = itemView.findViewById(R.id.productOptions)

        fun bind(product: Product?) {
            product?.let {
                itemView.setOnClickListener {
                    childClickCallback?.onChildSelected(product)
                }
                options.setOnClickListener {
                    childClickCallback?.onChildOptionsSelected(product)
                }
                name.text = it.name
                glide.load(it.image).into(image)
            }
        }
    }

    inner class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.categoryName)
        val image: ImageView = itemView.findViewById(R.id.categoryImage)
        val options: ImageView = itemView.findViewById(R.id.categoryOptions)

        fun bind(category: CategoryWithProducts?, groupPosition: Int) {
            itemView.setOnClickListener {
                flipExpandState(groupPosition)
            }
            category?.let {
                options.setOnClickListener {
                    //TODO: show options to delete product
                }
                name.text = it.category.name
                glide.load(it.category.image).into(image)
            }
        }
    }

    private fun flipExpandState(groupPosition: Int) {
        if (expandedGroupIndex == groupPosition) {
            collapseGroup()
        } else {
            expandGroup(groupPosition)
        }
    }

    fun expandGroup(groupPosition: Int) {
        expandedGroupIndex = groupPosition
        notifyDataSetChanged()
    }

    fun collapseGroup() {
        expandedGroupIndex = -1
        notifyDataSetChanged()
    }
}

interface ChildClickCallback {
    fun onChildSelected(product: Product)
    fun onChildOptionsSelected(product: Product)
}

