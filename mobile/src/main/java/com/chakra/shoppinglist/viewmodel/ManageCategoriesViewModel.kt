package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Category
import kotlinx.coroutines.launch

class ManageCategoriesViewModel(application: Application, repository: ShoppingPlanRepository)
    : AddProductViewModel(application, repository) {

    fun addCategory(category: Category): Boolean {
        viewModelScope.launch {
            repository.addCategory(category)
        }
    }

    fun renameCategory(category: Category, newName: String): Boolean {
        viewModelScope.launch {
            repository.renameCategory(category)
        }
    }

    fun deleteCategory(category: Category): Boolean {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }
}