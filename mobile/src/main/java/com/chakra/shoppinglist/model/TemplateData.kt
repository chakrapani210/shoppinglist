package com.chakra.shoppinglist.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TemplateData(
        val shoppingPlanTypes: List<ShoppingPlanType>
) : Parcelable