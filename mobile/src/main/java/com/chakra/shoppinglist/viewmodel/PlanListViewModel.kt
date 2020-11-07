package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.ShoppingPlan
import kotlinx.coroutines.launch

class PlanListViewModel(application: Application, val repository: ShoppingPlanRepository) : AndroidViewModel(application) {
    val shoppingListLiveData = MutableLiveData<List<ShoppingPlan>>()

    init {
        viewModelScope.launch {
            shoppingListLiveData.value = repository.getShoppingPlanList()
        }
    }

}