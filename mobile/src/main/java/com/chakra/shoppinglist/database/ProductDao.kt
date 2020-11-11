package com.chakra.shoppinglist.database

import android.content.Context
import androidx.room.*
import com.chakra.shoppinglist.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product WHERE name=:name")
    fun byName(name: String?): Product?

    @Query("SELECT * FROM Product WHERE id=:id")
    fun getProduct(id: Long?): Product?

    @Query("SELECT EXISTS(SELECT * FROM Product WHERE name=:name)")
    fun containsWithName(name: String?): Boolean

    @Insert
    fun insert(vararg products: Product?)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("DELETE FROM Product WHERE categoryId=:categoryId")
    fun deleteProductsOfCategory(categoryId: Long?)

    companion object {
        fun instance(context: Context): ProductDao {
            return AppDatabase.instance(context).productDao()
        }
    }
}