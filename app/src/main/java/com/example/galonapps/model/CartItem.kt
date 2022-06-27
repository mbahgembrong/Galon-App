package com.example.galonapps.model

data class CartItem(
    var galon: Galon,
    var quantity :Int = 0,
    var total : Int =0
)
