package com.chakra.shoppinglist.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.chakra.shoppinglist.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun all(): List<Category>?

    @Query("SELECT EXISTS(SELECT * FROM Category WHERE name=:name)")
    operator fun contains(name: String?): Boolean

    @Query("UPDATE Category SET name=:newName WHERE name=:oldName")
    fun rename(oldName: String?, newName: String?)

    @Insert
    fun insert(vararg categories: Category?)

    @Delete
    fun delete(category: Category?)

    companion object {
        fun instance(context: Context): CategoryDao {
            return AppDatabase.instance(context).categoryDao()
        }
    }
}