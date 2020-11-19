package com.chakra.shoppinglist.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Entity
@Parcelize
data class ShoppingPlanType(var name: String,
                            var color: String,
                            var isTemplate: Boolean,
                            var image: String?) : Parcelable {
    @field:PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Entity(foreignKeys = [
    ForeignKey(entity = ShoppingPlanType::class, parentColumns = ["id"], childColumns = ["planTypeId"], onDelete = ForeignKey.CASCADE)
])
data class ShoppingPlan(var name: String,
                        var planTypeId: Long?,
                        var totalItems: Int = 0,
                        var doneCount: Int = 0,
                        var createdDate: Calendar = Calendar.getInstance(),
                        var lastModifiedData: Calendar = Calendar.getInstance()) : Serializable {
    @field:PrimaryKey(autoGenerate = true)
    var id: Long = 0
}


/**
 * This hold the Products that are added to Plan
 */
@Entity(foreignKeys = [
    ForeignKey(entity = ShoppingPlan::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"], onDelete = ForeignKey.CASCADE)
])
data class Cart(var planId: Long,
                var productId: Long,
                var selected: Boolean,
                var quantity: Float) {
    @field:PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Entity(foreignKeys = [
    ForeignKey(entity = ShoppingPlanType::class, parentColumns = ["id"], childColumns = ["planTypeId"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"], onDelete = ForeignKey.CASCADE)
])
data class TopProducts(var planTypeId: Long?,
                       var productId: Int,
                       var count: Int) {
    @field:PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

data class CartWithProduct(
        @Embedded var cart: Cart,
        @Relation(
                parentColumn = "productId",
                entityColumn = "id"
        )
        var product: Product) {

    fun toggleSelection() {
        cart.selected = !cart.selected
    }
}

data class TopProductsInfo(
        @Embedded var topProducts: TopProducts,

        @Relation(
                parentColumn = "productId",
                entityColumn = "id"
        )
        var product: Product)

data class ShoppingPlanTypeWithTopProducts(
        @Embedded var planType: ShoppingPlanType,
        @Relation(
                entity = TopProducts::class,
                parentColumn = "id",
                entityColumn = "planTypeId")
        var topProductBucket: List<TopProductsInfo>?
)

data class ShoppingPlanWithCart(
        @Embedded var shoppingPlan: ShoppingPlan,
        @Relation(
                entity = Cart::class,
                parentColumn = "id",
                entityColumn = "planId")
        var cart: List<CartWithProduct>?,

        @Relation(
                parentColumn = "planTypeId",
                entityColumn = "id")
        var planType: ShoppingPlanType?
)

data class ShoppingPlanWithType(
        @Embedded var shoppingPlan: ShoppingPlan,

        @Relation(
                parentColumn = "planTypeId",
                entityColumn = "id")
        var planType: ShoppingPlanType?
)

/**
 * This hold the Products that are under a Category
 */
/*
@Entity
data class PlanCategoryProductList(var planId: Long,
                               var categoryId: Long)*/
