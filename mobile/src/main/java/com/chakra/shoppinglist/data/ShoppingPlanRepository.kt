package com.chakra.shoppinglist.data

import com.chakra.shoppinglist.database.CategoryDao
import com.chakra.shoppinglist.database.ProductDao
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.model.Product
import com.chakra.shoppinglist.model.ShoppingPlan
import com.chakra.shoppinglist.model.ShoppingPlanType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShoppingPlanRepository constructor(private val imageService: SearchImageService,
                                         private val productDao: ProductDao,
                                         private val categoryDao: CategoryDao) {

    suspend fun getShoppingPlanList() = withContext(Dispatchers.Default) {
        listOf(ShoppingPlan("Birth Day", ShoppingPlanType.BIRTHDAY_PARTY))
    }

    suspend fun getShoppingSuggestionsTypeList() = withContext(Dispatchers.Default) {
        ShoppingPlanType.values().asList()
    }

    suspend fun getProductList(category: String) = withContext(Dispatchers.Default) {
        val productList = productDao.byCategory(category, false)

        return@withContext productList?.sortedWith(Comparator { p1: Product, p2: Product ->
            p1.name().compareTo(p2.name())
        })
    }

    suspend fun getProductByNameList(name: String) = withContext(Dispatchers.Default) {
        productDao.byName(name)
    }

    suspend fun deleteProduct(product: Product) = withContext(Dispatchers.Default) {
        productDao.delete(product)
    }

    suspend fun productsInShoppingPlan() = withContext(Dispatchers.Default) {
        sortList(productDao.inCart())
    }

    private fun sortList(list: List<Product>?): List<Product>? {
        return list?.sortedWith(Comparator { p1: Product, p2: Product ->
            if (!p1.isSelected && p2.isSelected) {
                -1
            } else if (p1.isSelected && !p2.isSelected) {
                1
            } else {
                if (p1.category != p2.category) {
                    p1.category().compareTo(p2.category())
                } else {
                    p1.name().compareTo(p2.name())
                }
            }
        })
    }

    suspend fun setSelection(product: Product) = withContext(Dispatchers.Default) {
        productDao.setSelection(product.id(), product.isSelected)
    }

    suspend fun setSelection(productId: Int?, selected: Boolean?) = withContext(Dispatchers.Default) {
        productDao.moveToCart(productId, !selected!!, selected)
    }

    suspend fun moveToCart(product: Product) = withContext(Dispatchers.Default) {
        productDao.moveToCart(product.id(), true, false)
    }

    suspend fun removeFromCart(products: List<Product>) = withContext(Dispatchers.Default) {
        for (product in products) {
            productDao.moveToCart(product.id(), false, false)
        }
    }

    suspend fun getAllCategories() = withContext(Dispatchers.Default) {
        var categories = categoryDao.all()
        return@withContext categories?.sortedWith(Comparator { c1: Category, c2: Category -> c1.name().compareTo(c2.name()) })
    }

    suspend fun createProduct(newProduct: Product) = withContext(Dispatchers.Default) {
        if (!productDao.containsWithName(newProduct.name())) {
            productDao.insert(newProduct)
            return@withContext true
        }
        return@withContext false
    }

    suspend fun updateProduct(oldProduct: Product, newProduct: Product) = withContext(Dispatchers.Default) {
        val product: Product? = productDao.byName(newProduct.name())

        if (product == null || product.id() == oldProduct.id()) {
            productDao.update(oldProduct.id(), newProduct.name(), newProduct.category(), newProduct.image())
            return@withContext true
        }
        return@withContext false
    }

    /**
     * Category
     */
    suspend fun addCategory(category: Category) = withContext(Dispatchers.Default) {
        if (!categoryDao.contains(category.name())) {
            categoryDao.insert(category)
            return@withContext true
        }
        return@withContext false
    }

    suspend fun renameCategory(category: Category, newName: String) = withContext(Dispatchers.Default) {
        if (!categoryDao.contains(newName)) {
            categoryDao.rename(category.name(), newName)
            productDao.updateCategory(category.name(), newName)
            return@withContext true
        }
        return@withContext false
    }

    suspend fun deleteCategory(category: Category) = withContext(Dispatchers.Default) {
        if (!productDao.containsWithCategory(category.name())) {
            categoryDao.delete(category)
            return@withContext true
        }
        return@withContext false
    }

    // Image Search
    suspend fun searchImage(query: String) = withContext(Dispatchers.Default) {
        imageService.searchImages(query)
    }
}
