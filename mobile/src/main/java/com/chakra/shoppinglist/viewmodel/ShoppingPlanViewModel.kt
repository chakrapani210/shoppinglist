package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.ProductWithFullData
import com.chakra.shoppinglist.model.ShoppingPlan
import kotlinx.coroutines.launch

class ShoppingPlanViewModel(application: Application,
                            repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    val productsInPlan = MutableLiveData<List<ProductWithFullData>>()
    lateinit var shoppingPlan: ShoppingPlan

    fun reloadProductsInPlan(shoppingPlan: ShoppingPlan? = null) {
        viewModelScope.launch {
            shoppingPlan?.let {
                productsInPlan.value = repository.productsInShoppingPlan(it)
            }
        }
    }

    /*fun removeProduct(products: List<Product>) {
        viewModelScope.launch {
            repository.removeFromCart(products)
        }
    }*/

    fun setSelectedProduct(product: ProductWithFullData) {
        viewModelScope.launch {
            repository.updateCartItem(product.inCartProductData)
            reloadProductsInPlan()
        }
    }

    fun init(shoppingPlan: ShoppingPlan) {
        this.shoppingPlan = shoppingPlan
    }
}
