package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.CartWithProduct
import com.chakra.shoppinglist.model.ShoppingPlanWithType
import kotlinx.coroutines.launch

class ShoppingPlanViewModel(application: Application,
                            repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    lateinit var shoppingPlan: ShoppingPlanWithType

    fun init(shoppingPlan: ShoppingPlanWithType) {
        this.shoppingPlan = shoppingPlan
    }

    /*fun removeProduct(products: List<Product>) {
        viewModelScope.launch {
            repository.removeFromCart(products)
        }
    }*/

    fun toggleSelection(product: CartWithProduct) {
        viewModelScope.launch {
            product.toggleSelection()
            repository.updateCartItem(product.cart)
            if (product.cart.selected) {
                repository.updateCountOnProductDone(shoppingPlan.shoppingPlan)
            } else {
                repository.updateCountOnProductUndone(shoppingPlan.shoppingPlan)
            }
        }
    }
}
