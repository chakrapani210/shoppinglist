package com.chakra.shoppinglist.model

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Fts4
@Entity(indices = [Index(value = ["name"])])
class Product(var name: String,
              var image: String,
              var default: Boolean,
              var price: String,
              var currencySymbol: String,
              var categoryId: Long,
              @field:PrimaryKey(autoGenerate = true) val id: Long? = null) : Serializable {
    val isValid: Boolean
        get() = !TextUtils.isEmpty(name)
}