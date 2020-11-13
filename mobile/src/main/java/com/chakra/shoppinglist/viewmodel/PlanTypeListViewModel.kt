package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.ShoppingPlan
import com.chakra.shoppinglist.model.ShoppingPlanType
import kotlinx.coroutines.launch

class PlanTypeListViewModel(repository: ShoppingPlanRepository,
                            application: Application) : BaseViewModel(application, repository) {
    private var originalList: List<ShoppingPlanType>? = null
    val shoppingTypeSuggestions = MutableLiveData<List<ShoppingPlanType>?>()
    val shoppingPlanAddedLiveData = MutableLiveData<ShoppingPlan>()

    fun filterSuggestions(searchText: String?) {
        viewModelScope.launch() {
            if (searchText.isNullOrEmpty()) {
                shoppingTypeSuggestions.value = originalList
            }
            shoppingTypeSuggestions.value = repository.searchShoppingTypeList(searchText)
        }
    }

    fun addPlan(planName: String, planType: ShoppingPlanType?) {
        viewModelScope.launch {
            shoppingPlanAddedLiveData.value = repository.createPlan(ShoppingPlan(planName, planType?.id), null)
        }
    }

    init {
        viewModelScope.launch {
            originalList = repository.getShoppingSuggestionsTypeList()
            shoppingTypeSuggestions.value = originalList
        }
    }
}
