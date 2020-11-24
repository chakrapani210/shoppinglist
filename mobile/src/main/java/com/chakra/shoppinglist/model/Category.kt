package com.chakra.shoppinglist.model

import androidx.room.*
import java.io.Serializable
import java.util.*

@Entity
class Category(var name: String,
               var image: String? = null,
               var isTemplate: Boolean = true) : Serializable {

    @field:PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(id: Long) : this("", null, false) {
        this.id = id
    }

    companion object {
        fun withId(id: Long) = Category(id)
    }

    override fun toString(): String {
        return name
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        } else if (o == null || javaClass != o.javaClass) {
            return false
        }
        val category = o as Category
        return name == category.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

@Entity(primaryKeys = ["planTypeId", "productId"],
        foreignKeys = [
    ForeignKey(entity = ShoppingPlanType::class, parentColumns = ["id"], childColumns = ["planTypeId"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"], onDelete = ForeignKey.CASCADE)
])
data class RecentProduct(var planTypeId: Long,
                         var productId: Long,
                         var frequency: Int,
                         var lastSelected: Calendar)

data class CategoryWithProducts(
        @Embedded var category: Category,
        @Relation(
                parentColumn = "id",
                entityColumn = "categoryId"
        )
        var products: List<Product>?
)


data class RecentProductsInfo(
        @Embedded var recentProduct: RecentProduct,

        @Relation(
                parentColumn = "productId",
                entityColumn = "id"
        )
        var product: Product)

data class ShoppingPlanTypeWithRecentProducts(
        @Embedded var planType: ShoppingPlanType,
        @Relation(
                entity = RecentProduct::class,
                parentColumn = "id",
                entityColumn = "planTypeId"
        )
        var recents: List<RecentProductsInfo>
)