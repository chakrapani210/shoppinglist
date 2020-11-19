package com.chakra.shoppinglist.data

import androidx.lifecycle.Transformations
import com.chakra.shoppinglist.data.util.ColorUtil
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
        val productList = categoryDao.allProductsOf(categoryId)?.products

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

    suspend fun getShoppingPlanListItemData(shoppingPlan: ShoppingPlan) = withContext(Dispatchers.Default) {
        shoppingPlanDao.getShoppingPlanCartListItemData(shoppingPlan.id)
    }

    fun getProductsInShoppingPlan(shoppingPlan: ShoppingPlan?) = Transformations.map(shoppingPlanDao.getAllProductsOf(shoppingPlan?.id)) {
        it?.apply {
            cart = sortList(cart)
        } ?: null
    }

    private fun sortList(list: List<CartWithProduct>?): List<CartWithProduct>? {
        return list?.sortedWith(Comparator { p1: CartWithProduct, p2: CartWithProduct ->
            if (!p1.cart.selected && p2.cart.selected) {
                -1
            } else if (p1.cart.selected && !p2.cart.selected) {
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

    suspend fun updateCartItem(cart: Cart) = withContext(Dispatchers.Default) {
        shoppingPlanDao.update(cart)
    }

    suspend fun moveToCart(shoppingPlan: ShoppingPlanWithType, product: Product, quantity: Float = 1.0f) = withContext(Dispatchers.Default) {
        shoppingPlanDao.insert(Cart(shoppingPlan.shoppingPlan.id, product.id, false, quantity))
        updateOnProductAddedToCart(shoppingPlan.shoppingPlan)
    }

    suspend fun removeFromCart(shoppingPlan: ShoppingPlanWithType, product: Product) = withContext(Dispatchers.Default) {
        shoppingPlanDao.delete(Cart(shoppingPlan.shoppingPlan.id, product.id, true, 0.0f))
        updateOnProductRemovedFromCart(shoppingPlan.shoppingPlan)
    }

    suspend fun removeFromCart(products: List<Cart>) = withContext(Dispatchers.Default) {
        for (product in products) {
            shoppingPlanDao.delete(product)
        }
    }

    fun getAllCategories() = Transformations.map(categoryDao.all()) {
        it?.sortedWith(Comparator { c1: Category, c2: Category -> c1.name.compareTo(c2.name) })
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

    suspend fun createPlan(shoppingPlan: ShoppingPlan, image: String?): ShoppingPlanWithType? = withContext(Dispatchers.Default) {
        if (shoppingPlan.planTypeId == null) { // User didn't select Plan type. Generate new one.
            val planTypeIds = shoppingPlanDao.insert(ShoppingPlanType(shoppingPlan.name, ColorUtil.getRandomColor(), false, image))
            if (planTypeIds != null) {
                shoppingPlan.planTypeId = planTypeIds.get(0)
            }
        }
        shoppingPlanDao.insert(shoppingPlan)?.get(0)?.let {
            shoppingPlan.id = it
            return@withContext shoppingPlanDao.getShoppingPlanCartListItemData(shoppingPlan.id)
        }
        return@withContext null
    }

    suspend fun searchShoppingTypeList(searchText: String?)
            : List<ShoppingPlanType>? = withContext(Dispatchers.Default) {
        return@withContext shoppingPlanDao.searchShoppingTypeList(searchText)
    }

    suspend fun updateCountOnProductDone(shoppingPlan: ShoppingPlan) = withContext(Dispatchers.Default) {
        shoppingPlan.apply {
            doneCount += 1
            return@withContext shoppingPlanDao.updateShoppingPlan(this)
        }
    }

    suspend fun updateCountOnProductUndone(shoppingPlan: ShoppingPlan) = withContext(Dispatchers.Default) {
        shoppingPlan.apply {
            doneCount -= 1
            return@withContext shoppingPlanDao.updateShoppingPlan(this)
        }
    }

    private suspend fun updateOnProductRemovedFromCart(shoppingPlan: ShoppingPlan) = withContext(Dispatchers.Default) {
        shoppingPlan.apply {
            totalItems -= 1
            return@withContext shoppingPlanDao.updateShoppingPlan(this)
        }
    }

    private suspend fun updateOnProductAddedToCart(shoppingPlan: ShoppingPlan) = withContext(Dispatchers.Default) {
        shoppingPlan.apply {
            totalItems += 1
            return@withContext shoppingPlanDao.updateShoppingPlan(this)
        }
    }
}
