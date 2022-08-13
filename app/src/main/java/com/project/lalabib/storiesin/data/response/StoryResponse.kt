package com.project.lalabib.storiesin.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    val error: Boolean,

    val message: String,

    val listStory: List<ListStoryItem>,
)

@Parcelize
data class ListStoryItem(

    val name: String,
    @field:SerializedName("photoUrl")
    val photo: String,

    val description: String? = null,
): Parcelable
