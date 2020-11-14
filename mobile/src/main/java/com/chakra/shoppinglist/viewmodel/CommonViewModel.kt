package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.model.ShoppingPlanCartListItemData
import com.chakra.shoppinglist.model.ShoppingPlanWithCart

class CommonViewModel(application: Application, repository: ShoppingPlanRepository) : AndroidViewModel(application) {
    val searchImageSelected = MutableLiveData<String?>()
    var shoppingPlanCartListData = MutableLiveData<ShoppingPlanCartListItemData>()

    var shoppingPlanFullData: LiveData<ShoppingPlanWithCart?> = Transformations.switchMap(shoppingPlanCartListData) {
        it?.let {
            repository.getProductsInShoppingPlan(it.shoppingPlan)
        }
    }

    fun setSearchImageSelected(selectedImage: String?) {
        searchImageSelected.value = selectedImage
    }

    fun loadShoppingPlan(shoppingPlan: ShoppingPlanCartListItemData) {
        this.shoppingPlanCartListData.value = shoppingPlan
    }

    fun clearShoppingPlanSelection() {
        this.shoppingPlanCartListData.value = null
    }

    fun isProductAdded(product: Product) = shoppingPlanFullData.value?.cart?.filter { it.product.id == product.id } != null
}