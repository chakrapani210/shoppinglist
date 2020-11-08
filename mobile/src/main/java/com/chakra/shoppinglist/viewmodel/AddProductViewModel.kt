package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Category
import kotlinx.coroutines.launch

open class AddProductViewModel(application: Application,
                               repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    val categoryList = MutableLiveData<List<Category>?>()

    fun reloadCategories() {
        viewModelScope.launch {
            categoryList.value = repository.getAllCategories()
        }
    }

}