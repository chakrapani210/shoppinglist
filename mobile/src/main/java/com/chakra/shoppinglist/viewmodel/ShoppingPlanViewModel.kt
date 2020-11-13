package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.ProductWithFullData
import com.chakra.shoppinglist.model.ShoppingPlan
import kotlinx.coroutines.launch

class ShoppingPlanViewModel(application: Application,
                            repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    private val shoppingPlanLiveData = MutableLiveData<ShoppingPlan?>()
    val productsInPlan = Transformations.switchMap(shoppingPlanLiveData) {
        shoppingPlan.let {
            repository.productsInShoppingPlan(it)
        }
    }
    lateinit var shoppingPlan: ShoppingPlan

    fun reloadProductsInPlan() {
        shoppingPlanLiveData.value = shoppingPlan
    }

    /*fun removeProduct(products: List<Product>) {
        viewModelScope.launch {
            repository.removeFromCart(products)
        }
    }*/

    fun toggleSelection(product: ProductWithFullData) {
        viewModelScope.launch {
            product.toggleSelection()
            repository.updateCartItem(product.inCartProductData)
        }
    }

    fun init(shoppingPlan: ShoppingPlan) {
        this.shoppingPlan = shoppingPlan
    }
}
