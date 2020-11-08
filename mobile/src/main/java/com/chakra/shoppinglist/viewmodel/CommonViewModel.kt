package com.chakra.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class CommonViewModel(application: Application) : AndroidViewModel(application) {
    val searchImageSelected = MutableLiveData<String?>()

    fun setSearchImageSelected(selectedImage: String?) {
        searchImageSelected.value = selectedImage
    }
}