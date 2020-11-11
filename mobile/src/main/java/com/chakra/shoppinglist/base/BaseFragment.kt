package com.chakra.shoppinglist.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.chakra.shoppinglist.viewmodel.BaseViewModel

abstract class BaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(resourceLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onStart() {
        super.onStart()
        updateTitle()
        updateFloatingButton()
    }

    open fun updateTitle() {
        (requireActivity() as ShoppingPlannerActivity).title = getTitle()
    }

    open fun updateFloatingButton() {
        (requireActivity() as ShoppingPlannerActivity).updateFloatingButton(this)
    }

    protected fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    protected open fun initialize() {
        getBaseViewModel()?.errorMessage?.observe(viewLifecycleOwner) {
            it?.let {
                toast(it)
                getBaseViewModel()?.errorMessage?.value = null
            }
        }
    }

    abstract fun getBaseViewModel(): BaseViewModel?

    protected abstract fun getTitle(): String

    open fun onFloatingButtonClicked() {
    }

    open fun isFloatingButtonEnabled() = false

    protected abstract val resourceLayoutId: Int
}