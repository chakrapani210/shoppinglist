package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.ProductWithFullData
import com.chakra.shoppinglist.model.ShoppingPlanCartListItemData
import kotlinx.coroutines.launch

class ShoppingPlanViewModel(application: Application,
                            repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    lateinit var shoppingPlan: ShoppingPlanCartListItemData

    fun init(shoppingPlan: ShoppingPlanCartListItemData) {
        this.shoppingPlan = shoppingPlan
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
            if (product.inCartProductData.selected) {
                repository.updateCountOnProductDone(shoppingPlan.inCartProductCountData)
            } else {
                repository.updateCountOnProductUndone(shoppingPlan.inCartProductCountData)
            }
        }
    }
}
