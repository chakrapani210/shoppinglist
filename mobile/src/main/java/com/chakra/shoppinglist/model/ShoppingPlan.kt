package com.chakra.shoppinglist.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable
import java.util.*

@Entity
data class ShoppingPlanType(@field:PrimaryKey(autoGenerate = true) val id: Long?,
                            val name: String,
                            val default: Boolean,
                            val image: String?)

@Entity
data class ShoppingPlan(val name: String,
                        var shoppingPlanTypeId: Long?,
                        val createdDate: Calendar = Calendar.getInstance(),
                        val lastModifiedData: Calendar = Calendar.getInstance(),
                        @field:PrimaryKey(autoGenerate = true) var id: Long? = null) : Serializable {
    val isValid: Boolean
        get() = name.isNullOrEmpty()
}

/**
 * This hold the Products that are added to Plan
 */
@Entity
data class InCartProductData(val planId: Long,
                             val productId: Long,
                             var selected: Boolean,
                             val quantity: Float,
                             @field:PrimaryKey(autoGenerate = true) val id: Long? = null)

@Entity
data class InCartProductCountData(val planId: Long?,
                                  val totalItems: Int,
                                  val doneCount: Int,
                                  @field:PrimaryKey(autoGenerate = true) val id: Long? = null)

data class ProductWithFullData(
        @Embedded val product: Product,
        @Relation(
                parentColumn = "id",
                entityColumn = "productId"
        )
        val inCartProductData: InCartProductData
) {
    fun toggleSelection() {
        inCartProductData.selected = !inCartProductData.selected
    }
}

data class ShoppingPlanWithCart(
        @Embedded val shoppingPlan: ShoppingPlan,
        @Relation(
                entity = InCartProductData::class,
                parentColumn = "id",
                entityColumn = "planId"
        )
        val cart: List<ProductWithFullData>,

        @Relation(
                parentColumn = "shoppingPlanTypeId",
                entityColumn = "id"
        )
        val planType: ShoppingPlanType
)

data class ShoppingPlanCartListItemData(
        @Embedded val shoppingPlan: ShoppingPlan,
        @Relation(
                parentColumn = "id",
                entityColumn = "planId"
        )
        val inCartProductCountData: InCartProductCountData,

        @Relation(
                parentColumn = "shoppingPlanTypeId",
                entityColumn = "id"
        )
        val planType: ShoppingPlanType
)

/**
 * This hold the Products that are under a Category
 */
/*
@Entity
data class PlanCategoryProductList(val planId: Long,
                               val categoryId: Long)*/
