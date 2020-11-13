package com.chakra.shoppinglist.database

import android.content.Context
import androidx.room.*
import com.chakra.shoppinglist.model.Category
import com.chakra.shoppinglist.model.CategoryWithProducts

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun all(): List<Category>?

    @Query("SELECT EXISTS(SELECT * FROM Category WHERE id=:id)")
    fun containsById(id: Long?): Boolean

    @Query("SELECT EXISTS(SELECT * FROM Category WHERE name=:name)")
    fun containsByName(name: String): Boolean

    @Query("UPDATE Category SET name=:newName WHERE id=:id")
    fun rename(id: Long?, newName: String?)

    @Insert
    fun insert(vararg categories: Category?): List<Long>?

    @Delete
    fun delete(category: Category?)

    @Transaction
    @Query("SELECT * FROM Category WHERE id=:categoryId")
    fun allProductsOf(categoryId: Long): CategoryWithProducts?

    companion object {
        fun instance(context: Context): CategoryDao {
            return AppDatabase.instance(context).categoryDao()
        }
    }
}