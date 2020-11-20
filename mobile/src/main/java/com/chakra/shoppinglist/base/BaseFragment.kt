package com.chakra.shoppinglist.base

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.chakra.shoppinglist.viewmodel.BaseViewModel
import kotlinx.android.parcel.Parcelize

abstract class BaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(resourceLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
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

    @Parcelize
    internal class NavigationResult(var requestKey: String, var resultCode: Int, var data: Bundle) : Parcelable

    /**
     * Equivalent to Activity results.
     * This method adds observer to listen for results with #request key.
     * When other fragments send results back with #requestKey, you get callback with @onNavigationResults() method.
     */
    protected open fun observeForResult(requestKey: String) {
        val navController: NavController = NavHostFragment.findNavController(this)
        val savedStateHandle: SavedStateHandle? = navController.currentBackStackEntry?.savedStateHandle
        val liveData: MutableLiveData<NavigationResult>? = savedStateHandle?.getLiveData(requestKey)
        liveData?.observe(viewLifecycleOwner, Observer { result: NavigationResult? ->
            if (result != null) {
                onNavigationResults(result.requestKey, result.resultCode, result.data)
                savedStateHandle.remove<Any>(result.requestKey)
            }
        })
    }

    private fun onNavigationResults(requestKey: String, resultCode: Int, data: Bundle?) {}

    /**
     * Equivalent to Activity results.
     * You get this callback, when other fragments send results with #requestKey, #resultCode, and #data
     * protected open fun onNavigationResults (requestKey: String?, resultCode: Int, data: Bundle?) ()
     * use this method to send results to previous fragment in the backstack.
     */
    protected open fun sendResultToPreviousScreen(requestKey: String, resultCode: Int, data: Bundle) {
        val navController: NavController = NavHostFragment.findNavController(this)
        navController.previousBackStackEntry?.apply {
            savedStateHandle[requestKey] = NavigationResult(requestKey, resultCode, data)
        }
    }

    /**
     *  use this method to send results to a perticular #destinationId fragment in the backstack.
     */
    protected open fun sendResultToDestination(destinationId: Int, requestKey: String, resultCode: Int, data: Bundle) {
        val navController: NavController = NavHostFragment.findNavController(this)
        navController.getBackStackEntry(destinationId).apply {
            savedStateHandle[requestKey] = NavigationResult(requestKey, resultCode, data)
        }
    }

    protected fun moveToPreviousScreen() {
        requireActivity().onBackPressed()
    }

    abstract fun getBaseViewModel(): BaseViewModel?

    abstract fun getTitle(): String

    open fun enableBack(): Boolean = true

    open fun onFloatingButtonClicked() {
    }

    open fun isFloatingButtonEnabled() = false

    protected abstract val resourceLayoutId: Int
}