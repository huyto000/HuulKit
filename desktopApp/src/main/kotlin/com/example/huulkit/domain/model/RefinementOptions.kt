package com.example.huulkit.domain.model

/**
 * Domain model class to encapsulate refinement options
 */
data class RefinementOptions(
    val shorten: Boolean = false,
    val clarify: Boolean = false,
    val makeKinder: Boolean = false,
    val polish: Boolean = false,
    val combineAll: Boolean = false
)