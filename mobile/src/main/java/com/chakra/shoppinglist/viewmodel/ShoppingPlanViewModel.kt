package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.model.ShoppingPlan
import kotlinx.coroutines.launch

class ShoppingPlanViewModel(application: Application,
                            private val repository: ShoppingPlanRepository) : AndroidViewModel(application) {
    val productsInPlan = MutableLiveData<List<Product>>()

    private fun reloadProductsInPlan(shoppingPlan: ShoppingPlan? = null) {
        viewModelScope.launch {
            productsInPlan.value = sortList(repository.productsInShoppingPlan())
        }
    }

    private fun sortList(list: MutableList<Product>?): List<Product>? {
        list?.sortWith { p1: Product, p2: Product ->
            if (!p1.isSelected && p2.isSelected) {
                return@sortWith -1
            } else if (p1.isSelected && !p2.isSelected) {
                return@sortWith 1
            } else {
                if (p1.category != p2.category) {
                    return@sortWith p1.category().compareTo(p2.category())
                } else {
                    return@sortWith p1.name().compareTo(p2.name())
                }
            }
        }
        return list
    }

    fun removeProduct(products: List<Product>) {
        viewModelScope.launch {
            repository.removeFromCart(products)
        }
    }

    fun setSelectedProduct(product: Product) {
        viewModelScope.launch {
            repository.setSelection(product)
            reloadProductsInPlan()
        }
    }

    fun init(shoppingPlan: ShoppingPlan?) {
        reloadProductsInPlan(shoppingPlan)
    }
}
