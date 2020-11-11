package com.chakra.shoppinglist.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable
import java.util.*

enum class ShoppingPlanStatus {
    OPEN,
    DONE,
    DELETED
}

@Entity
data class ShoppingPlanType(@field:PrimaryKey(autoGenerate = true) val id: Long,
                            val name: String,
                            val image: String?)

@Entity
data class ShoppingPlan(val name: String,
                        val shoppingPlanTypeId: Long,
                        val statusId: Int,
                        val createdDate: Calendar,
                        val lastModifiedData: Calendar,
                        @field:PrimaryKey(autoGenerate = true) val id: Long? = null) : Serializable {

    var status: ShoppingPlanStatus = ShoppingPlanStatus.values()[this.statusId]

    constructor(name: String,
                shoppingPlanTypeId: Long,
                status: ShoppingPlanStatus = ShoppingPlanStatus.OPEN,
                lastModifiedData: Calendar = Calendar.getInstance(),
                createdDate: Calendar = Calendar.getInstance(),
                id: Long? = null)
            : this(name, shoppingPlanTypeId, status.ordinal, createdDate, lastModifiedData, id)

    val isValid: Boolean
        get() = name.isNullOrEmpty()
}

/**
 * This hold the Products that are added to Plan
 */
@Entity
data class InCartProductData(val planId: Long,
                             val productId: Long,
                             val selected: Boolean,
                             val quantity: Float,
                             @field:PrimaryKey(autoGenerate = true) val id: Long? = null)

data class ProductWithFullData(
        @Embedded val product: Product,
        @Relation(
                parentColumn = "id",
                entityColumn = "productId"
        )
        val inCartProductData: InCartProductData
)

data class ShoppingPlanWithCart(
        @Embedded val shoppingPlan: ShoppingPlan,
        @Relation(
                entity = InCartProductData::class,
                parentColumn = "id",
                entityColumn = "planId"
        )
        val cart: List<ProductWithFullData>
)

/**
 * This hold the Products that are under a Category
 */
/*
@Entity
data class PlanCategoryProductList(val planId: Long,
                               val categoryId: Long)*/
