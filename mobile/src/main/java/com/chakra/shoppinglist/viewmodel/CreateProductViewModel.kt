package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Product
import kotlinx.coroutines.launch

class CreateProductViewModel(application: Application,
                             repository: ShoppingPlanRepository) : AddProductViewModel(application, repository) {
    val updateResult = MutableLiveData<Boolean>()

    fun updateProduct(newProduct: Product) {
        viewModelScope.launch {
            if (repository.updateProduct(newProduct)) {
                updateResult.value = true
            } else {
                setError(R.string.error_category_already_exists)
            }
        }
    }

    fun createProduct(newProduct: Product) {
        viewModelScope.launch {
            if (repository.createProduct(newProduct)) {
                updateResult.value = true
            } else {
                setError(R.string.error_category_already_exists)
            }
        }
    }
}