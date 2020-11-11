package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.model.ShoppingPlan
import kotlinx.coroutines.launch

class ShoppingPlanViewModel(application: Application,
                            repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    val productsInPlan = MutableLiveData<List<Product>>()
    lateinit var shoppingPlan: ShoppingPlan

    fun reloadProductsInPlan(shoppingPlan: ShoppingPlan? = null) {
        viewModelScope.launch {
            productsInPlan.value = repository.productsInShoppingPlan(shoppingPlan)
        }
    }

    fun removeProduct(products: List<Product>) {
        viewModelScope.launch {
            repository.removeFromCart(products)
        }
    }

    fun setSelectedProduct(product: Product) {
        viewModelScope.launch {
            repository.updateCartItem(product)
            reloadProductsInPlan()
        }
    }

    fun init(shoppingPlan: ShoppingPlan) {
        this.shoppingPlan = shoppingPlan
    }
}
