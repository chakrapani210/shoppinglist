package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.ShoppingPlanWithType

open class AddProductViewModel(application: Application,
                               repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    lateinit var shoppingPlan: ShoppingPlanWithType
    private val reloadCategories = MutableLiveData<Boolean>()
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