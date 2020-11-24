package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.*
import kotlinx.coroutines.launch

class CategoriesViewModel(application: Application,
                          repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {

    private lateinit var shoppingPlan: ShoppingPlanWithType
    private val loadLiveData = MutableLiveData<Boolean>()
    val categoriesWithProductsLiveData = Transformations.switchMap(loadLiveData) { load ->
        load?.let {
            if (it) {
                repository.getCategoriesWithProducts()
            } else {
                null
            }
        }
    }

    init {
        loadLiveData.value = true
    }

    fun init(shoppingPlan: ShoppingPlanWithType) {
        this.shoppingPlan = shoppingPlan
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            repository.removeFromCart(shoppingPlan, product)
        }
    }

    fun moveToCart(product: Product) {
        viewModelScope.launch {
            repository.moveToCart(shoppingPlan, product)
        }
    }
}