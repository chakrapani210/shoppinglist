package com.chakra.shoppinglist.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

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

data class CategoryWithProducts(
        @Embedded var category: Category,
        @Relation(
                parentColumn = "id",
                entityColumn = "categoryId"
        )
        var products: List<Product>
)