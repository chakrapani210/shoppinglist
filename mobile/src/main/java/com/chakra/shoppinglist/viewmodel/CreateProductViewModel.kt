package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Product
import kotlinx.coroutines.launch

class CreateProductViewModel(application: Application,
                             repository: ShoppingPlanRepository) : AddProductViewModel(application, repository) {
    val updateResult = MutableLiveData<Boolean>()
    fun updateProduct(oldProduct: Product, newProduct: Product) {
        viewModelScope.launch {
            updateResult.value = repository.updateProduct(oldProduct, newProduct)
        }
    }

    fun createProduct(newProduct: Product) {
        viewModelScope.launch {
            updateResult.value = repository.createProduct(newProduct)
        }
    }
}