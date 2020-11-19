package com.chakra.shoppinglist.data.util

import kotlin.random.Random

object ColorUtil {
    private val BIRDS_OF_FEATHER = arrayOf("#5C1FA7", " #F5439B", "#E5DF55", "#47C73D", " #346AC0",
            "#f2476a", "#fb654e", "#eb2d3a", "#B82D2F", "#6DBD3D")

    fun getRandomColor() = BIRDS_OF_FEATHER[Random.nextInt(BIRDS_OF_FEATHER.size)]
}
