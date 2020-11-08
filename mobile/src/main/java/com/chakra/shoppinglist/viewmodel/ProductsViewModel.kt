package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Product
import kotlinx.coroutines.launch

class ProductsViewModel(application: Application,
                        repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    val productsLiveData = MutableLiveData<List<Product>?>()
    private var category: String? = null

    fun reloadProducts(category: String) {
        this.category = category
        viewModelScope.launch {
            productsLiveData.postValue(repository.getProductList(category))
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            category?.let {
                reloadProducts(it)
            }
        }
    }

    fun moveToCart(product: Product) {
        viewModelScope.launch {
            repository.moveToCart(product)
            category?.let {
                reloadProducts(it)
            }
        }

    }
}