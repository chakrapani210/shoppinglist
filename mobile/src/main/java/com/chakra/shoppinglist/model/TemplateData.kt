package com.chakra.shoppinglist.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TemplateData(
        @SerializedName("shoppingPlanTypes")
        val shoppingPlanTypes: List<ShoppingPlanType>
) : Parcelable