package com.chakra.shoppinglist.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.observe
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.base.ShoppingPlannerActivity
import com.chakra.shoppinglist.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.screen_search_image.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchImageFragment : BaseFragment() {
    companion object {
        private const val PARAM_INITIAL_QUERY = "initial.query"

        fun getDataBundle(initialQuery: String?) = Bundle().apply {
            putString(PARAM_INITIAL_QUERY, initialQuery)
        }
    }

    private lateinit var adapter: SearchImageAdapter

    private val viewModel: SearchViewModel by viewModel()

    override fun getBaseViewModel() = viewModel

    override fun getTitle() = getString(R.string.search_image)

    override val resourceLayoutId: Int
        get() = R.layout.screen_search_image

    override fun initialize() {
        loadingMode()
        toolbarClose.setOnClickListener {
            onClose()
        }

        toolbarInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId === EditorInfo.IME_ACTION_SEARCH) {
                onSearch()
                return@setOnEditorActionListener true
            }
            false
        }
        toolbarInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                toolbarSearch.isClickable = !TextUtils.isEmpty(toolbarInput.text)
            }
        })
        adapter = SearchImageAdapter(requireContext())
        grid.adapter = adapter
        grid.setOnItemClickListener { parent, view, position, id ->
            onImageSelected(parent.getItemAtPosition(position) as String)
        }

        query(arguments?.getString(PARAM_INITIAL_QUERY))

        viewModel.searchImageList.observe(viewLifecycleOwner) {
            loadImages(it)
        }
    }

    private fun onSearch() {
        val query: String = toolbarInput.text.toString()
        if (!TextUtils.isEmpty(query)) {
            viewModel.onSearch(query)
        }
    }

    private fun onImageSelected(imageSelection: String) {
        (requireActivity() as ShoppingPlannerActivity)
                .commonViewModel.setSearchImageSelected(imageSelection)
        moveToPreviousScreen()
    }

    private fun onClose() {
        requireActivity().onBackPressed()
    }

    fun query(initialQuery: String?) {
        initialQuery?.let {
            toolbarInput.setText(it)
            if (it.isNotEmpty()) {
                toolbarInput.setSelection(it.length)
            }
            onSearch()
        }
    }

    private fun loadImages(images: List<String>?) {
        adapter.loadImages(images)
        presentationMode()
    }

    private fun loadingMode() {
        grid.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    private fun presentationMode() {
        grid.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }

}