package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import kotlinx.coroutines.launch

class SearchViewModel(application: Application,
                      repository: ShoppingPlanRepository) : BaseViewModel(application, repository) {
    val searchImageList = MutableLiveData<List<String>>()

    fun onSearch(query: String) {
        viewModelScope.launch {
            searchImageList.value = repository.searchImage(query)
        }
    }
}