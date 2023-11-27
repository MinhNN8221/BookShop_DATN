package com.example.BookShopApp.data.model.response

import com.example.BookShopApp.data.model.Rating
import com.google.gson.annotations.SerializedName

data class RatingResponse(
    @SerializedName("totalRating" ) var totalRating : Int?,
    @SerializedName("ratingDtos"  ) var ratingDtos  : List<Rating>,
)
