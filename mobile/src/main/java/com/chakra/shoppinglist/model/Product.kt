package com.chakra.shoppinglist.model

import androidx.room.*
import java.io.Serializable

@Entity(indices = [Index(value = ["name"])],
        foreignKeys = [ForeignKey(entity = Category::class, parentColumns = ["id"],
                childColumns = ["categoryId"], onDelete = ForeignKey.CASCADE)]
)
class Product(var name: String,
              var image: String,
              @field:ColumnInfo(name = "isTemplate") var isTemplate: Boolean = true,
              var categoryId: Long,
              var price: String = "") : Serializable {
    @field:PrimaryKey(autoGenerate = true)
    var id: Long = 0
}