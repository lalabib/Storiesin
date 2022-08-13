package com.project.lalabib.storiesin.utils

import com.project.lalabib.storiesin.data.response.*

object DataDummy {

    fun generateDummyListStory(): List<ListStoryItem> {
        val items = arrayListOf<ListStoryItem>()

        for (i in 0 until 10) {
            val story = ListStoryItem(
                "Dimas",
                "story-FvU4u0Vp2S3PMsFg",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "2022-01-08T06:34:18.598Z",
                "Lorem Ipsum",
                -16.002,
                -10.212
            )

            items.add(story)
        }
        return items
    }

    fun generateDummyStoriesWithLocResponse(): StoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "name + $i",
                i.toString(),
                "description + $i",
                "created + $i",
                "id + $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return StoryResponse(
            items,
            false,
            "success"
        )
    }

    fun generateDummyLoginResponse(): LoginResponse {
        return LoginResponse(
            LoginResult(
                "name",
                "token"
            ),
            false,
            "token"
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            false,
           "success"
        )
    }

    fun generateDummyUploadStoryResponse(): AddStoryResponse {
        return AddStoryResponse(
            false,
            "success"
        )
    }
}