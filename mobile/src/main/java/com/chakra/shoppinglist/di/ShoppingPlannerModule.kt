package com.chakra.shoppinglist.di

import com.bumptech.glide.Glide
import com.chakra.shoppinglist.data.SearchImageService
import com.chakra.shoppinglist.data.ShoppingPlanRepository
import com.chakra.shoppinglist.database.CategoryDao
import com.chakra.shoppinglist.database.ProductDao
import com.chakra.shoppinglist.database.ShoppingPlanDao
import com.chakra.shoppinglist.viewmodel.*
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val shoppingPlannerModule: Module = module {

    //DAO
    single { ProductDao.instance(androidContext()) }
    single { CategoryDao.instance(androidContext()) }
    single { ShoppingPlanDao.instance(androidContext()) }

    // = OkHttpClient()
    single { OkHttpClient() }
    single { Glide.with(androidContext()) }

    // Serivce
    single { SearchImageService(get()) }

    // Single instance of Repository
    single { ShoppingPlanRepository(get(), get(), get(), get()) }

    // declare a scope for DetailActivity
    /*scope(named<ShoppingPlannerActivity>()) {
        //scoped<DetailContract.Presenter> { DetailPresenter(get(), get()) }
        viewModel { AddProductViewModel(androidApplication(), get()) }
    }*/
    // ViewModel instance of MyViewModel
    // get() will resolve Repository instance
    /*scope<ShoppingPlannerActivity> {
        scoped<CommonViewModel> { CommonViewModel(get(), get()) }
    }*/
    viewModel { AddProductViewModel(androidApplication(), get()) }
    viewModel { CommonViewModel(androidApplication(), get()) }
    viewModel { CreateProductViewModel(androidApplication(), get()) }
    viewModel { ManageCategoriesViewModel(androidApplication(), get()) }
    viewModel { PlanListViewModel(androidApplication(), get()) }
    viewModel { PlanTypeListViewModel(get(), androidApplication()) }
    viewModel { TopProductsViewModel(androidApplication(), get()) }
    viewModel { SearchViewModel(androidApplication(), get()) }
    viewModel { ShoppingPlanViewModel(androidApplication(), get()) }
    viewModel { CategoriesViewModel(androidApplication(), get()) }
    viewModel { RecentProductsViewModel(androidApplication(), get()) }
}