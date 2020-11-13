package com.chakra.shoppinglist.fragments

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chakra.shoppinglist.utils.ResourceUtils

class SearchImageAdapter(private val context: Context) : BaseAdapter() {
    private val images: MutableList<String> = mutableListOf()
    private val imageLoader: RequestManager = Glide.with(context)

    fun loadImages(list: List<String>?) {
        images.clear()
        list?.let {
            images.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Any {
        return images[position]
    }

    override fun getItemId(position: Int): Long {
        return images[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            val size: Int = ResourceUtils.getDisplayMetrics(context).widthPixels / 3
            val padding = (size.toFloat() / 20f).toInt()
            imageView = ImageView(context)
            imageView.scaleType = ScaleType.CENTER_INSIDE
            imageView.layoutParams = AbsListView.LayoutParams(size, size)
            imageView.scaleType = ScaleType.CENTER_CROP
            imageView.setPadding(padding, padding, padding, padding)
        } else {
            imageView = convertView as ImageView
        }
        imageLoader.load(images[position]).into(imageView)
        return imageView
    }
}