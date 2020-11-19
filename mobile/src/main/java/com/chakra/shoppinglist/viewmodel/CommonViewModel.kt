package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.model.ShoppingPlanWithCart
import com.chakra.shoppinglist.model.ShoppingPlanWithType

class CommonViewModel(application: Application, repository: ShoppingPlanRepository) : AndroidViewModel(application) {
    var color = MutableLiveData<String?>()
    val searchImageSelected = MutableLiveData<String?>()
    var shoppingPlanCartListData = MutableLiveData<ShoppingPlanWithType>()
    var productIdSet: HashSet<Long> = HashSet()

    var shoppingPlanFullData: LiveData<ShoppingPlanWithCart?> = Transformations.switchMap(shoppingPlanCartListData) {
        it?.let {
            repository.getProductsInShoppingPlan(it.shoppingPlan)
        }
    }

    init {
        shoppingPlanFullData.observeForever {
            generateProductIdSet(it)
        }
    }

    private fun generateProductIdSet(shoppingData: ShoppingPlanWithCart?) {
        shoppingData?.cart?.forEach {
            productIdSet.add(it.product.id)
        }
    }

    fun setSearchImageSelected(selectedImage: String?) {
        searchImageSelected.value = selectedImage
    }

    fun loadShoppingPlan(shoppingPlan: ShoppingPlanWithType) {
        this.shoppingPlanCartListData.value = shoppingPlan
        this.color.value = shoppingPlan.planType?.color
    }

    fun clearShoppingPlanSelection() {
        this.shoppingPlanCartListData.value = null
    }

    fun isProductAdded(product: Product) = productIdSet.contains(product.id)
    fun addProductId(id: Long) {
        productIdSet.add(id)
    }

    fun removeProductId(id: Long) {
        productIdSet.remove(id)
    }
}