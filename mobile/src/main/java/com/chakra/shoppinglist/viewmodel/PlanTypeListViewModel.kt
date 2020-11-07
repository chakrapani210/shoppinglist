package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.model.ShoppingPlan
import com.chakra.shoppinglist.model.ShoppingPlanType
import kotlinx.coroutines.launch

class PlanTypeListViewModel(private val repository: ShoppingPlanRepository,
                            application: Application) : AndroidViewModel(application) {
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

    fun addPlan(planName: String, planType: ShoppingPlanType) {
        // TODO: do handle database changes
        shoppingPlanAddedLiveData.value = ShoppingPlan(planName, planType)
    }

    init {
        viewModelScope.launch {
            originalList = repository.getShoppingSuggestionsTypeList()
            shoppingTypeSuggestions.value = repository.getShoppingSuggestionsTypeList()
        }
    }
}
