package com.chakra.shoppinglist.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.chakra.shoppinglist.model.*

@Dao
interface ShoppingPlanDao {
    @Transaction
    @Query("SELECT * FROM ShoppingPlan")
    fun all(): LiveData<List<ShoppingPlanWithType>?>

    @Transaction
    @Query("SELECT * FROM ShoppingPlan WHERE id=:id")
    fun getShoppingPlanCartListItemData(id: Long?): ShoppingPlanWithType?

    @Query("SELECT EXISTS(SELECT * FROM ShoppingPlan WHERE name=:name)")
    operator fun contains(name: String?): Boolean

    @Query("UPDATE ShoppingPlan SET name=:newName WHERE name=:oldName")
    fun rename(oldName: String?, newName: String?)

    @Update
    fun updateShoppingPlan(shoppingPlan: ShoppingPlan)

    @Insert
    fun insert(vararg shoppingPlans: ShoppingPlan?): List<Long>?

    @Delete
    fun delete(shoppingPlan: ShoppingPlan?)

    /*** ShoppingPlanWithCart ***/

    @Transaction
    @Query("SELECT * FROM ShoppingPlan WHERE id=:shoppingPlanId")
    fun getAllProductsOf(shoppingPlanId: Long?): LiveData<ShoppingPlanWithCart?>

    @Insert
    fun insert(vararg cartPlanProduct: Cart): List<Long>?

    @Update
    fun update(cartPlanProduct: Cart)

    @Delete
    fun delete(cartPlanProduct: Cart)

    /*** ShoppingPlanType ***/

    @Query("SELECT * FROM ShoppingPlanType")
    fun getAllShoppingPlanTypes(): List<ShoppingPlanType>?

    @Insert
    fun insert(vararg shoppingPlanType: ShoppingPlanType): List<Long>?

    @Update
    fun update(shoppingPlanType: ShoppingPlanType)

    @Delete
    fun delete(shoppingPlanType: ShoppingPlanType)

    @Query("SELECT * FROM ShoppingPlanType WHERE name LIKE '%' || :searchText || '%'")
    fun searchShoppingTypeList(searchText: String?): List<ShoppingPlanType>?

    /** Top Product **/
    @Transaction
    @Query("SELECT * FROM ShoppingPlanType WHERE id=:planTypeId")
    fun getTopProductsOf(planTypeId: Long?): ShoppingPlanTypeWithTopProducts

    companion object {
        fun instance(context: Context): ShoppingPlanDao {
            return AppDatabase.instance(context).shoppingPlanDao()
        }
    }
}