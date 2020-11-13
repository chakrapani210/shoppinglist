package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.model.ShoppingPlan
import kotlinx.coroutines.launch

class ProductsViewModel(application: Application,
                        repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    val productsLiveData = MutableLiveData<List<Product>?>()
    lateinit var category: Category
    lateinit var shoppingPlan: ShoppingPlan
    val title get() = category.name

    fun init(shoppingPlan: ShoppingPlan, category: Category) {
        this.shoppingPlan = shoppingPlan
        this.category = category
        reloadProducts()
    }

    fun reloadProducts() {
        viewModelScope.launch {
            productsLiveData.postValue(repository.getProductListForCategory(category.id!!))
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
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