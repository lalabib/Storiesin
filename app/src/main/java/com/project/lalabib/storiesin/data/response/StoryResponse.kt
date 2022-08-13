package com.project.lalabib.storiesin.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(

    val listStory: List<ListStoryItem>,

    val error: Boolean,

    val message: String,
)

@Parcelize
data class ListStoryItem(

    val name: String,

    val id: String,

    @field:SerializedName("photoUrl")
    val photo: String,

    val createdAt: String,

    val description: String? = null,

    val lon: Double,

    val lat: Double
): Parcelable
