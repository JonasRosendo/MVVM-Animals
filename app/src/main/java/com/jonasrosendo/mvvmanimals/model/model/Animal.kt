package com.jonasrosendo.mvvmanimals.model.model

data class Animal (
    val name: String?,
    val taxonomy: Taxonomy?,
    val location: String?,
    val speed: Speed?,
    val diet: String?,
    val lifespan: String?,
    val image: String?
)

data class Taxonomy(
    val kingdom: String?,
    val order: String?,
    val family: String?
)

data class Speed(
    val metric: String?,
    val imperial: String?
)