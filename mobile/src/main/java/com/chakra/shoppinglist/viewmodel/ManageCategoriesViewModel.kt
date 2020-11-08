package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Category
import kotlinx.coroutines.launch

class ManageCategoriesViewModel(application: Application,
                                repository: ShoppingPlanRepository) : AddProductViewModel(application, repository) {

    fun addCategory(category: Category) {
        viewModelScope.launch {
            if (repository.addCategory(category)) {
                reloadCategories()
            } else {
                errorMessage.value = getApplication<Application>().getString(R.string.error_category_already_exists)
            }
        }
    }

    fun renameCategory(category: Category, newName: String) {
        viewModelScope.launch {
            if (repository.renameCategory(category, newName)) {
                reloadCategories()
            } else {
                errorMessage.value = getApplication<Application>().getString(R.string.error_category_already_exists)
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            if (repository.deleteCategory(category)) {
                reloadCategories()
            } else {
                errorMessage.value = getApplication<Application>().getString(R.string.error_category_in_use)
            }
        }
    }
}