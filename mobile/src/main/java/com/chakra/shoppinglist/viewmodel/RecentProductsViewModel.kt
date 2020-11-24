package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.*
import kotlinx.coroutines.launch

class RecentProductsViewModel(application: Application,
                              repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    val productsLiveData = MutableLiveData<ShoppingPlanTypeWithRecentProducts?>()
    lateinit var shoppingPlan: ShoppingPlanWithType

    fun init(shoppingPlan: ShoppingPlanWithType) {
        this.shoppingPlan = shoppingPlan
        reloadProducts()
    }

    fun reloadProducts() {
        viewModelScope.launch {
            productsLiveData.value = repository.getRecents(shoppingPlan.planType.id)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            reloadProducts()
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            repository.removeFromCart(shoppingPlan, product)
            reloadProducts()
        }
    }

    fun moveToCart(product: Product) {
        viewModelScope.launch {
            repository.moveToCart(shoppingPlan, product)
            reloadProducts()
        }
    }
}
