package com.chakra.shoppinglist.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.chakra.shoppinglist.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product WHERE inCart")
    fun inCart(): List<Product>?

    @Query("SELECT * FROM Product WHERE category=:category AND inCart=:inCart")
    fun byCategory(category: String?, inCart: Boolean?): List<Product>?

    @Query("SELECT * FROM Product WHERE name=:name")
    fun byName(name: String?): Product?

    @Query("SELECT EXISTS(SELECT * FROM Product WHERE name=:name)")
    fun containsWithName(name: String?): Boolean

    @Query("SELECT EXISTS(SELECT * FROM Product WHERE category=:category)")
    fun containsWithCategory(category: String?): Boolean

    @Query("UPDATE Product SET category=:newCategory WHERE category=:oldCategory")
    fun updateCategory(oldCategory: String?, newCategory: String?)

    @Query("UPDATE Product SET name=:name, category=:category, image=:image WHERE id=:id")
    fun update(id: Int?, name: String?, category: String?, image: String?)

    @Query("UPDATE Product SET selected=:selected WHERE id=:id")
    fun setSelection(id: Int?, selected: Boolean?)

    @Query("UPDATE Product SET inCart=:inCart, selected=:selected WHERE id=:id")
    fun moveToCart(id: Int?, inCart: Boolean?, selected: Boolean?)

    @Insert
    fun insert(vararg products: Product?)

    @Delete
    fun delete(product: Product?)

    companion object {
        fun instance(context: Context): ProductDao {
            return AppDatabase.instance(context).productDao()
        }
    }
}