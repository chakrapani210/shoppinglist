package com.chakra.shoppinglist.model

import android.text.TextUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(indices = [Index(value = ["name"])])
class Product(var name: String,
              var image: String,
              @field:ColumnInfo(name = "isTemplate") var isTemplate: Boolean = true,
              var categoryId: Long? = null,
              var price: String? = "",
              @field:PrimaryKey(autoGenerate = true) var id: Long? = null) : Serializable {
    val isValid: Boolean
        get() = !TextUtils.isEmpty(name)
}