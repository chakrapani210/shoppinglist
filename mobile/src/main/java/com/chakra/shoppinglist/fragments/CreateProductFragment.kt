package com.chakra.shoppinglist.fragments

import android.Manifest.permission
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.activities.ManageCategoriesActivity
import com.chakra.shoppinglist.activities.SearchImageActivity
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.utils.ResourceUtils.fileFromUri
import com.chakra.shoppinglist.utils.ResourceUtils.uri
import com.chakra.shoppinglist.utils.takePictureIntent
import com.chakra.shoppinglist.viewmodel.CreateProductViewModel
import com.chakra.shoppinglist.views.Dialogs
import kotlinx.android.synthetic.main.screen_create_product.*
import java.util.*
import kotlin.properties.Delegates

class CreateProductFragment : BaseFragment() {
    companion object {
        private const val PARAM_PRODUCT = "product"
        private const val PARAM_CATEGORY = "category"
        private const val DEFAULT_IMAGE = "https://i.imgur.com/ztA411S.png"

        private const val CAMERA_PERMISSION = 1001
        private const val READ_DISK_PERMISSION = 1002

        private const val MANAGE_CATEGORIES_REQUEST_CODE = 2000
        private const val CAMERA_IMAGE_REQUEST_CODE = 2001
        private const val GALLERY_IMAGE_REQUEST_CODE = 2002
        private const val SEARCH_IMAGE_REQUEST_CODE = 2003

        fun getDataBundle(context: Context?, product: Product?) = Bundle().apply {
            putSerializable(PARAM_PRODUCT, product)
        }

        fun getDataBundle(context: Context?, category: String?) = Bundle().apply {
            putString(PARAM_CATEGORY, category)
        }
    }

    private lateinit var viewModel: CreateProductViewModel
    private var isCreateNew by Delegates.notNull<Boolean>()
    private lateinit var selectedImage: String
    private var cameraUri: Uri? = null

    override val resourceLayoutId: Int
        get() = R.layout.screen_create_product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(viewModelStore,
                ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
                .get(CreateProductViewModel::class.java)
        isCreateNew = arguments?.getSerializable(PARAM_PRODUCT) as Product? == null
    }

    override fun getTitle(): String = if (true) {
        getString(R.string.toolbar_title_create_product)
    } else {
        getString(R.string.toolbar_title_edit_product)
    }

    private fun category(): String {
        val element: Any = categoryList.selectedItem
        return element.toString()
    }

    fun name(): String {
        return name.text.toString()
    }

    override fun initialize() {
        if (isCreateNew) {
            buttonAction.setText(R.string.button_create)
            loadImage(DEFAULT_IMAGE)
        } else {
            productAddToCard.isChecked = false
            productAddToCard.visibility = View.GONE
            buttonAction.setText(R.string.button_update)
        }

        buttonAction.setOnClickListener {
            onAction(category(), name(), selectedImage, productAddToCard.isChecked)
        }

        productImage.setOnClickListener {
            onChangeImage()
        }

        productImageChange.setOnClickListener {
            onChangeImage()
        }

        categoryManage.setOnClickListener {
            onManageCategories()
        }

        viewModel.categoryList.observe(viewLifecycleOwner, { list ->
            list?.let {
                var category: String? = null
                var product: Product? = null
                arguments?.let { bundle ->
                    category = bundle.getString(PARAM_CATEGORY, "")
                    product = bundle.getSerializable(PARAM_PRODUCT) as Product?
                }
                loadCategoryList(it, category, product)
            }
        })

        viewModel.updateResult.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    requireActivity().onBackPressed()
                } else {
                    toast(getString(R.string.error_product_already_exists))
                }
            }

        })
    }

    private fun loadCategoryList(categories: List<Category?>, category: String?, product: Product?) {
        val adapter: ArrayAdapter<Category?> = ArrayAdapter<Category?>(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryList.adapter = adapter
        if (product != null) {
            categoryList.setSelection(categories.indexOf(Category(product.category())))
            name.setText(product.name())
            loadImage(product.image())
        } else if (!TextUtils.isEmpty(category)) {
            categoryList.setSelection(categories.indexOf(Category(category)))
        }
    }

    private fun loadImage(imageUrl: String?) {
        imageUrl?.let {
            selectedImage = imageUrl
            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(productImage)
        }
    }

    private fun onChangeImage() {
        val options = Arrays.asList(
                getString(R.string.label_source_camera),
                getString(R.string.label_source_gallery),
                getString(R.string.label_source_search)
        )

        val dialogs = Dialogs(requireContext())
        dialogs.options(getString(R.string.label_source_select), options) { option: Int ->
            when (option) {
                0 -> imageFromCamera()
                1 -> imageFromGallery()
                2 -> imageFromSearch()
            }
        }
    }

    private fun imageFromCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || requireActivity().checkSelfPermission(permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission.CAMERA), CAMERA_PERMISSION)
        }
    }

    private fun imageFromGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || requireActivity().checkSelfPermission(permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission.READ_EXTERNAL_STORAGE), READ_DISK_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionsCameraGranted()
                }
            }

            READ_DISK_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionsReadStorageGranted()
                }
            }
        }
    }

    private fun onPermissionsCameraGranted() {
        try {
            cameraUri = uri(requireContext())
            cameraUri?.let {
                startActivityForResult(takePictureIntent(requireContext(), it), CAMERA_IMAGE_REQUEST_CODE)
            }
        } catch (e: Exception) {
            toast(getString(R.string.error_openingCamera))
        }
    }

    private fun onPermissionsReadStorageGranted() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE)
    }

    private fun imageFromSearch() {
        val intent = SearchImageActivity.intent(requireContext(), name())
        startActivityForResult(intent, SEARCH_IMAGE_REQUEST_CODE)
    }

    private fun onManageCategories() {
        val intent = Intent(requireContext(), ManageCategoriesActivity::class.java)
        startActivityForResult(intent, MANAGE_CATEGORIES_REQUEST_CODE)
    }

    private fun onAction(category: String, name: String, image: String, inCart: Boolean) {
        clearError()

        if (TextUtils.isEmpty(name)) {
            missingName()
        } else {
            val oldProduct: Product? = arguments?.getSerializable(PARAM_PRODUCT) as Product
            val newProduct = Product(category, name, image, inCart, false)
            if (oldProduct != null) {
                viewModel.updateProduct(oldProduct, newProduct)
            } else {
                viewModel.createProduct(newProduct)
            }
        }
    }

    private fun clearError() {
        nameHeader.error = ""
    }

    private fun missingName() {
        nameHeader.error = requireContext().getString(R.string.error_invalid_name)
    }

    private fun processUriImage(uri: Uri?) {
        try {
            val file = fileFromUri(requireContext(), uri!!)
            loadImage(file.absolutePath)
        } catch (e: java.lang.Exception) {
            toast(getString(R.string.error_loadingImage))
        }
    }

    private fun processSearchImage(imageUrl: String?) {
        loadImage(imageUrl)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            processUriImage(cameraUri)
        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            processUriImage(data?.data)
        } else if (requestCode == SEARCH_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            processSearchImage(data?.getStringExtra(SearchImageActivity.PARAM_IMAGE_URL))
        } else if (requestCode == MANAGE_CATEGORIES_REQUEST_CODE) {
            viewModel.reloadCategories()
        }
    }
}
