package com.chakra.shoppinglist.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity
class Category(val name: String,
               val image: String,
               val default: Boolean,
               @field:PrimaryKey(autoGenerate = true) val id: Long? = null) : Serializable {
    fun name(): String {
        return name
    }

    override fun toString(): String {
        return name()
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
        @Embedded val category: Category,
        @Relation(
                parentColumn = "id",
                entityColumn = "categoryId"
        )
        val cart: List<Product>
)