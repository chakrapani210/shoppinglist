package com.chakra.shoppinglist.database

import android.content.Context
import androidx.room.*
import com.chakra.shoppinglist.model.InCartProductData
import com.chakra.shoppinglist.model.ShoppingPlan
import com.chakra.shoppinglist.model.ShoppingPlanType
import com.chakra.shoppinglist.model.ShoppingPlanWithCart

@Dao
interface ShoppingPlanDao {
    @Query("SELECT * FROM ShoppingPlan")
    fun all(): List<ShoppingPlan>?

    @Query("SELECT EXISTS(SELECT * FROM ShoppingPlan WHERE name=:name)")
    operator fun contains(name: String?): Boolean

    @Query("UPDATE ShoppingPlan SET name=:newName WHERE name=:oldName")
    fun rename(oldName: String?, newName: String?)

    @Update
    fun updateShoppingPlan(shoppingPlan: ShoppingPlan)

    @Insert
    fun insert(vararg shoppingPlans: ShoppingPlan?)

    @Delete
    fun delete(shoppingPlan: ShoppingPlan?)

    @Transaction
    @Query("SELECT * FROM ShoppingPlan WHERE id=:shoppingPlanId")
    fun getAllProductsOf(shoppingPlanId: Long?): ShoppingPlanWithCart?

    @Insert
    fun insert(vararg inCartPlanProduct: InCartProductData)

    @Update
    fun update(inCartPlanProduct: InCartProductData)

    @Delete
    fun delete(inCartPlanProduct: InCartProductData)

    @Query("SELECT * FROM ShoppingPlanType")
    fun getAllShoppingPlanTypes(): List<ShoppingPlanType>?

    @Insert
    fun insert(vararg shoppingPlanType: ShoppingPlanType)

    @Update
    fun update(shoppingPlanType: ShoppingPlanType)

    @Delete
    fun delete(shoppingPlanType: ShoppingPlanType)

    companion object {
        fun instance(context: Context): ShoppingPlanDao {
            return AppDatabase.instance(context).shoppingPlanDao()
        }
    }
}