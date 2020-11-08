package com.chakra.shoppinglist.data

import com.chakra.shoppinglist.database.CategoryDao
import com.chakra.shoppinglist.database.ProductDao
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.model.ShoppingPlan
import com.chakra.shoppinglist.model.ShoppingPlanType

class ShoppingPlanRepository constructor(private val imageService: SearchImageService,
                                         private val productDao: ProductDao,
                                         private val categoryDao: CategoryDao) {

    fun getShoppingPlanList(): List<ShoppingPlan>? {
        return listOf(ShoppingPlan("Birth Day", ShoppingPlanType.BIRTHDAY_PARTY))
    }

    fun getShoppingSuggestionsTypeList(): List<ShoppingPlanType>? {
        return ShoppingPlanType.values().asList()
    }

    fun getProductList(category: String): List<Product>? {
        val productList = productDao.byCategory(category, false)
        return productList?.sortedWith { p1: Product, p2: Product ->
            p1.name().compareTo(p2.name())
        }
    }

    fun getProductByNameList(name: String) = productDao.byName(name)

    fun deleteProduct(product: Product) {
        productDao.delete(product)
    }

    fun productsInShoppingPlan() = sortList(productDao.inCart())

    private fun sortList(list: List<Product>?): List<Product>? {
        return list?.sortedWith { p1: Product, p2: Product ->
            if (!p1.isSelected && p2.isSelected) {
                return@sortedWith -1
            } else if (p1.isSelected && !p2.isSelected) {
                return@sortedWith 1
            } else {
                if (p1.category != p2.category) {
                    return@sortedWith p1.category().compareTo(p2.category())
                } else {
                    return@sortedWith p1.name().compareTo(p2.name())
                }
            }
        }
    }

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

    fun getAllCategories(): List<Category>? {
        var categories = categoryDao.all()
        return categories?.sortedWith { c1: Category, c2: Category -> c1.name().compareTo(c2.name()) }
    }

    fun createProduct(newProduct: Product): Boolean {
        if (!productDao.containsWithName(newProduct.name())) {
            productDao.insert(newProduct)
            return true
        }
        return false
    }

    fun updateProduct(oldProduct: Product, newProduct: Product): Boolean {
        val product: Product? = productDao.byName(newProduct.name())

        if (product == null || product.id() == oldProduct.id()) {
            productDao.update(oldProduct.id(), newProduct.name(), newProduct.category(), newProduct.image())
            return true
        }
        return false
    }

    /**
     * Category
     */
    fun addCategory(category: Category): Boolean {
        if (!categoryDao.contains(category.name())) {
            categoryDao.insert(category)
            return true
        }
        return false
    }

    fun renameCategory(category: Category, newName: String): Boolean {
        if (!categoryDao.contains(newName)) {
            categoryDao.rename(category.name(), newName)
            productDao.updateCategory(category.name(), newName)
            return true
        }
        return false
    }

    fun deleteCategory(category: Category): Boolean {
        if (!productDao.containsWithCategory(category.name())) {
            categoryDao.delete(category)
            return true
        }
        return false
    }

    // Image Search
    fun searchImage(query: String) = imageService.searchImages(query)
}