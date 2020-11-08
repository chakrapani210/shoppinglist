package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.chakra.shoppinglist.data.ShoppingPlanRepository

open class BaseViewModel(application: Application, val repository: ShoppingPlanRepository) : AndroidViewModel(application) {
    val errorMessage = MutableLiveData<String?>()

    protected fun setError(errorId: Int) {
        setError(getApplication<Application>().getString(errorId))
    }

    protected fun setError(error: String?) {
        errorMessage.value = error
    }
}