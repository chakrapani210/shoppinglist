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
        if (searchText == null) {
            shoppingTypeSuggestions.value = originalList
            return
        }
        shoppingTypeSuggestions.value?.let {
            shoppingTypeSuggestions.value = it.filter { shoppingPlanType ->
                getApplication<Application>().getString(shoppingPlanType.nameResId).contains(searchText, true)
            }
        }
    }

    fun addPlan(planName: String, planType: ShoppingPlanType?) {
        viewModelScope.launch {
            shoppingPlanAddedLiveData.value = repository.createPlan(ShoppingPlan(planName, planType?.id))
        }
    }

    init {
        viewModelScope.launch {
            originalList = repository.getShoppingSuggestionsTypeList()
            shoppingTypeSuggestions.value = repository.getShoppingSuggestionsTypeList()
        }
    }
}
