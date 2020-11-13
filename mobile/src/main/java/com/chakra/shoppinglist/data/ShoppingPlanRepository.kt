package com.chakra.shoppinglist.data

import com.chakra.shoppinglist.database.CategoryDao
import com.chakra.shoppinglist.database.ProductDao
import com.chakra.shoppinglist.database.ShoppingPlanDao
import com.chakra.shoppinglist.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShoppingPlanRepository constructor(private val imageService: SearchImageService,
                                         private val shoppingPlanDao: ShoppingPlanDao,
                                         private val productDao: ProductDao,
                                         private val categoryDao: CategoryDao) {

    fun getShoppingPlanList() = shoppingPlanDao.all()

    suspend fun getShoppingSuggestionsTypeList() = withContext(Dispatchers.Default) {
        shoppingPlanDao.getAllShoppingPlanTypes()
    }

    suspend fun getProductListForCategory(categoryId: Long) = withContext(Dispatchers.Default) {
        val productList = categoryDao.allProductsOf(categoryId)?.cart

        return@withContext productList?.sortedWith(Comparator { p1: Product, p2: Product ->
            p1.name.compareTo(p2.name)
        })
    }

    suspend fun getProductByNameList(name: String) = withContext(Dispatchers.Default) {
        productDao.byName(name)
    }

    suspend fun deleteProduct(product: Product) = withContext(Dispatchers.Default) {
        productDao.delete(product)
    }

    fun productsInShoppingPlan(shoppingPlan: ShoppingPlan?) = shoppingPlanDao.getAllProductsOf(shoppingPlan?.id)

    private fun sortList(list: List<ProductWithFullData>?): List<ProductWithFullData>? {
        return list?.sortedWith(Comparator { p1: ProductWithFullData, p2: ProductWithFullData ->
            if (!p1.inCartProductData.selected && p2.inCartProductData.selected) {
                -1
            } else if (p1.inCartProductData.selected && !p2.inCartProductData.selected) {
                1
            } else {
                /*if (p1.product.category != p2.product.category) {
                    p1.category().compareTo(p2.category())
                } else {*/
                p1.product.name.compareTo(p2.product.name)
                //}
            }
        })
    }

    suspend fun updateCartItem(inCartProductData: InCartProductData) = withContext(Dispatchers.Default) {
        shoppingPlanDao.update(inCartProductData)
    }

    suspend fun moveToCart(shoppingPlan: ShoppingPlan, product: Product, quantity: Float = 1.0f) = withContext(Dispatchers.Default) {
        shoppingPlanDao.insert(InCartProductData(shoppingPlan.id!!, product.id!!, true, quantity))
    }

    suspend fun removeFromCart(products: List<InCartProductData>) = withContext(Dispatchers.Default) {
        for (product in products) {
            shoppingPlanDao.delete(product)
        }
    }

    suspend fun getAllCategories() = withContext(Dispatchers.Default) {
        var categories = categoryDao.all()
        return@withContext categories?.sortedWith(Comparator { c1: Category, c2: Category -> c1.name.compareTo(c2.name) })
    }

    suspend fun createProduct(newProduct: Product) = withContext(Dispatchers.Default) {
        if (!productDao.containsWithName(newProduct.name)) {
            productDao.insert(newProduct)
            return@withContext true
        }
        return@withContext false
    }

    suspend fun updateProduct(newProduct: Product) = withContext(Dispatchers.Default) {
        val product: Product? = productDao.byName(newProduct.name)

        if (product == null || product.id == newProduct.id) {
            productDao.update(newProduct)
            return@withContext true
        }

        return@withContext false
    }

    /**
     * Category
     */
    suspend fun addCategory(category: Category) = withContext(Dispatchers.Default) {
        if (!categoryDao.containsByName(category.name)) {
            categoryDao.insert(category)
            return@withContext true
        }
        return@withContext false
    }

    suspend fun renameCategory(category: Category, newName: String) = withContext(Dispatchers.Default) {
        if (!categoryDao.containsByName(newName)) {
            categoryDao.rename(category.id, newName)
            return@withContext true
        }
        return@withContext false
    }

    suspend fun deleteCategory(category: Category) = withContext(Dispatchers.Default) {
        if (categoryDao.containsById(category.id)) {
            categoryDao.delete(category)
            productDao.deleteProductsOfCategory(category.id)
            return@withContext true
        }
        return@withContext false
    }

    // Image Search
    suspend fun searchImage(query: String) = withContext(Dispatchers.Default) {
        imageService.searchImages(query)
    }

    suspend fun createPlan(shoppingPlan: ShoppingPlan, image: String?): ShoppingPlan = withContext(Dispatchers.Default) {
        if (shoppingPlan.shoppingPlanTypeId == null) {
            val planTypeId = shoppingPlanDao.insert(ShoppingPlanType(null, shoppingPlan.name, false, image))
            shoppingPlan.shoppingPlanTypeId = planTypeId?.get(0)
        }
        shoppingPlan.id = shoppingPlanDao.insert(shoppingPlan)?.get(0)

        shoppingPlanDao.insert(InCartProductCountData(shoppingPlan.id, 0, 0))
        return@withContext shoppingPlan
    }

    suspend fun searchShoppingTypeList(searchText: String?)
            : List<ShoppingPlanType>? = withContext(Dispatchers.Default) {
        return@withContext shoppingPlanDao.searchShoppingTypeList(searchText)
    }
}
