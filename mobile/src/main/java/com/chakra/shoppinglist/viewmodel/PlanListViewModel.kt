package com.chakra.shoppinglist.viewmodel

import android.app.Application
import com.chakra.shoppinglist.data.ShoppingPlanRepository

class PlanListViewModel(application: Application, repository: ShoppingPlanRepository)
    : BaseViewModel(application, repository) {
    var autoMOveToPlanTypeListScreen = true
    val shoppingListLiveData = repository.getShoppingPlanList()

    /*init {
        viewModelScope.launch {
            shoppingListLiveData =
        }
    }*/
}