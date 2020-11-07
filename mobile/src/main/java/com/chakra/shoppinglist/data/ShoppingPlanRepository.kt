package com.chakra.shoppinglist.data

import com.chakra.shoppinglist.database.CategoryDao
import com.chakra.shoppinglist.database.ProductDao
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.model.ShoppingPlan
import com.chakra.shoppinglist.model.ShoppingPlanType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingPlanRepository @Inject constructor(private val productDao: ProductDao, private val categoryDao: CategoryDao) {
    fun getShoppingPlanList(): List<ShoppingPlan>? {
        return listOf(ShoppingPlan("Birth Day", ShoppingPlanType.BIRTHDAY_PARTY))
    }

    fun getShoppingSuggestionsTypeList(): List<ShoppingPlanType>? {
        return ShoppingPlanType.values().asList()
    }

    fun getProductList(category: String): MutableList<Product>? {
        val productList = productDao.byCategory(category, false)
        productList.sortWith { p1: Product, p2: Product ->
            p1.name().compareTo(p2.name())
        }
        return productList
    }

    fun getProductByNameList(name: String) = productDao.byName(name)

    fun deleteProduct(product: Product) {
        productDao.delete(product)
    }

    fun productsInShoppingPlan() = productDao.inCart()

    fun setSelection(product: Product) {
        productDao.setSelection(product.id(), product.isSelected)
    }

    fun setSelection(productId: Int?, selected: Boolean?) {
        productDao.moveToCart(productId, !selected!!, selected)
    }

    fun moveToCart(product: Product) {
        productDao.moveToCart(product.id(), true, false)
    }

    fun removeFromCart(products: List<Product>) {
        for (product in products) {
            productDao.moveToCart(product.id(), false, false)
        }
    }

    fun getAllCategories(): List<Category?> {
        var categories = categoryDao.all()
        return categories.sortedWith { c1: Category, c2: Category -> c1.name().compareTo(c2.name()) }
    }

    fun createProduct(newProduct: Product): Boolean {
        if (!productDao.containsWithName(newProduct.name())) {
            productDao.insert(newProduct)
            return true
        }
        return false
    }

    fun updateProduct(oldProduct: Product, newProduct: Product): Boolean {
        val product: Product = productDao.byName(newProduct.name())

        if (product == null || product.id() == oldProduct.id()) {
            productDao.update(oldProduct.id(), newProduct.name(), newProduct.category(), newProduct.image())
            return true
        }
        return false
    }
}