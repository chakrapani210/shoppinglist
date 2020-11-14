package com.chakra.shoppinglist.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable
import java.util.*

@Entity
data class ShoppingPlanType(@field:PrimaryKey(autoGenerate = true) var id: Long?,
                            var name: String,
                            var isTemplate: Boolean,
                            var image: String?)

@Entity
data class ShoppingPlan(var name: String,
                        var shoppingPlanTypeId: Long?,
                        var createdDate: Calendar = Calendar.getInstance(),
                        var lastModifiedData: Calendar = Calendar.getInstance(),
                        @field:PrimaryKey(autoGenerate = true) var id: Long? = null) : Serializable {
    val isValid: Boolean
        get() = name.isNullOrEmpty()
}

/**
 * This hold the Products that are added to Plan
 */
@Entity(primaryKeys = ["planId", "productId"])
data class InCartProductData(var planId: Long,
                             var productId: Long,
                             var selected: Boolean,
                             var quantity: Float)

@Entity
data class InCartProductCountData(@field:PrimaryKey var planId: Long?,
                                  var totalItems: Int,
                                  var doneCount: Int)

data class ProductWithFullData(
        @Embedded var inCartProductData: InCartProductData,
        @Relation(
                parentColumn = "productId",
                entityColumn = "id"
        )
        var product: Product
) {
    fun toggleSelection() {
        inCartProductData.selected = !inCartProductData.selected
    }
}

data class ShoppingPlanWithCart(
        @Embedded var shoppingPlan: ShoppingPlan,
        @Relation(
                entity = InCartProductData::class,
                parentColumn = "id",
                entityColumn = "planId"
        )
        var cart: List<ProductWithFullData>?,

        @Relation(
                parentColumn = "shoppingPlanTypeId",
                entityColumn = "id"
        )
        var planType: ShoppingPlanType?
)

data class ShoppingPlanCartListItemData(
        @Embedded var shoppingPlan: ShoppingPlan,
        @Relation(
                parentColumn = "id",
                entityColumn = "planId"
        )
        var inCartProductCountData: InCartProductCountData,

        @Relation(
                parentColumn = "shoppingPlanTypeId",
                entityColumn = "id"
        )
        var planType: ShoppingPlanType?
)

/**
 * This hold the Products that are under a Category
 */
/*
@Entity
data class PlanCategoryProductList(var planId: Long,
                               var categoryId: Long)*/
