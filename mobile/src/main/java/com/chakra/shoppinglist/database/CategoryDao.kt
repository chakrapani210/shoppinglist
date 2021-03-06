package com.chakra.shoppinglist.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.chakra.shoppinglist.model.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun all(): LiveData<List<Category>?>

    @Query("SELECT EXISTS(SELECT * FROM Category WHERE id=:id)")
    fun containsById(id: Long?): Boolean

    @Query("SELECT EXISTS(SELECT * FROM Category WHERE name=:name)")
    fun containsByName(name: String): Boolean

    @Query("UPDATE Category SET name=:newName WHERE id=:id")
    fun rename(id: Long?, newName: String?)

    @Insert
    fun insert(vararg categories: Category): List<Long>

    @Delete
    fun delete(category: Category?)

    @Transaction
    @Query("SELECT * FROM Category WHERE id=:categoryId")
    fun allProductsOf(categoryId: Long): CategoryWithProducts?

    @Transaction
    @Query("SELECT * FROM Category")
    fun allCategoriesWithProducts(): LiveData<List<CategoryWithProducts>?>

    // Recents
    @Insert
    fun insertRecentProduct(recentSelection: RecentProduct)

    @Update
    fun updateRecentProduct(recentSelection: RecentProduct)

    @Query("SELECT * FROM RecentProduct WHERE planTypeId=:planTypeId AND productId=:productId")
    fun getRecentSelection(planTypeId: Long, productId: Long): RecentProduct

    @Transaction
    @Query("SELECT * FROM ShoppingPlanType WHERE id=:planTypeId")
    fun getRecents(planTypeId: Long): ShoppingPlanTypeWithRecentProducts

    companion object {
        fun instance(context: Context): CategoryDao {
            return AppDatabase.instance(context).categoryDao()
        }
    }
}