package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.ShoppingPlanTypeWithRecentProducts
import com.chakra.shoppinglist.model.ShoppingPlanWithType
import kotlinx.coroutines.launch

open class AddProductViewModel(application: Application,
                               repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    val recentProductsLiveData = MutableLiveData<ShoppingPlanTypeWithRecentProducts?>()
    lateinit var shoppingPlan: ShoppingPlanWithType
    private val reloadCategories = MutableLiveData<Boolean>()

    fun init(shoppingPlan: ShoppingPlanWithType) {
        this.shoppingPlan = shoppingPlan
    }

    fun loadRecentProducts() {
        viewModelScope.launch {
            recentProductsLiveData.value = repository.getRecents(shoppingPlan.planType.id)
        }
    }

    val categoryList = Transformations.switchMap(reloadCategories) {
        if (it != null && it) {
            repository.getAllCategories()
        } else {
            null
        }
    }

    fun reloadCategories() {
        reloadCategories.value = true
    }

}