package com.chakra.shoppinglist.database

import android.content.Context
import androidx.room.*
import com.chakra.shoppinglist.model.*

@Dao
interface ShoppingPlanDao {
    @Transaction
    @Query("SELECT * FROM ShoppingPlan")
    fun all(): List<ShoppingPlanCartListItemData>?

    @Query("SELECT EXISTS(SELECT * FROM ShoppingPlan WHERE name=:name)")
    operator fun contains(name: String?): Boolean

    @Query("UPDATE ShoppingPlan SET name=:newName WHERE name=:oldName")
    fun rename(oldName: String?, newName: String?)

    @Update
    fun updateShoppingPlan(shoppingPlan: ShoppingPlan)

    @Insert
    fun insert(vararg shoppingPlans: ShoppingPlan?): Long?

    @Delete
    fun delete(shoppingPlan: ShoppingPlan?)

    /*** ShoppingPlanWithCart ***/

    @Transaction
    @Query("SELECT * FROM ShoppingPlan WHERE id=:shoppingPlanId")
    fun getAllProductsOf(shoppingPlanId: Long?): ShoppingPlanWithCart?

    @Insert
    fun insert(vararg inCartPlanProduct: InCartProductData): Long?

    @Update
    fun update(inCartPlanProduct: InCartProductData)

    @Delete
    fun delete(inCartPlanProduct: InCartProductData)

    /*** ShoppingPlanType ***/

    @Query("SELECT * FROM ShoppingPlanType")
    fun getAllShoppingPlanTypes(): List<ShoppingPlanType>?

    @Insert
    fun insert(vararg shoppingPlanType: ShoppingPlanType): Long?

    @Update
    fun update(shoppingPlanType: ShoppingPlanType)

    @Delete
    fun delete(shoppingPlanType: ShoppingPlanType)

    /*** InCartProductCountData ***/
    @Insert
    fun insert(vararg inCartProductCountData: InCartProductCountData): Long?

    @Update
    fun update(inCartProductCountData: InCartProductCountData)

    @Delete
    fun delete(inCartProductCountData: InCartProductCountData)

    companion object {
        fun instance(context: Context): ShoppingPlanDao {
            return AppDatabase.instance(context).shoppingPlanDao()
        }
    }
}