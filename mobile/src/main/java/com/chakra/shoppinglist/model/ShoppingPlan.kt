package com.chakra.shoppinglist.model

import com.chakra.shoppinglist.R
import java.io.Serializable


enum class ShoppingPlanType(val nameResId: Int, val imageResourceId: Int? = null) {
    GROCERIES(R.string.plan_type_groceries),
    BIRTHDAY_PARTY(R.string.plan_type_birth_day_party),
    PARTY(R.string.plan_type_party),
    MARRIAGE(R.string.plan_type_marriage),
    MARRIAGE_ANNIVERSARY(R.string.plan_type_marriage_anniversary),
    GARMENTS(R.string.plan_type_garments),
    STYLE_SHOPPING(R.string.plan_type_style_shopping),
    HALLOWEEN_SHOPPING(R.string.plan_type_halloween_shopping),
    THANKS_GIVING_SHOPPING(R.string.plan_type_thanks_giving),
    CHRISTMAS_SHOPPING(R.string.plan_type_christmas),
    OTHER(R.string.plan_type_groceries)
}

enum class ShoppingPlanStatus {
    OPEN,
    DONE,
    DELETED
}

data class ShoppingPlan(val name: String,
                        val shoppingPlan: ShoppingPlanType,
                        val status: ShoppingPlanStatus = ShoppingPlanStatus.OPEN,
                        val id: Int? = null,
                        val lastModifiedData: Long? = null,
                        val createdData: Long? = null) : Serializable