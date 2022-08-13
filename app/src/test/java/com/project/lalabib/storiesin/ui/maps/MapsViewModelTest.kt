package com.project.lalabib.storiesin.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.project.lalabib.storiesin.data.Result
import com.project.lalabib.storiesin.utils.DataDummy
import com.project.lalabib.storiesin.data.StoryRepository
import com.project.lalabib.storiesin.data.response.StoryResponse
import com.project.lalabib.storiesin.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStoryWithLoc = DataDummy.generateDummyStoriesWithLocResponse()
    private val token = "TOKEN"

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when Get ListStory With Location is Success`() {
        val expectedStory = MutableLiveData<Result<StoryResponse>>()
        expectedStory.value = Result.Success(dummyStoryWithLoc)
        Mockito.`when`(mapsViewModel.getStoryWithLocation(token)).thenReturn(expectedStory)

        val actualStory = mapsViewModel.getStoryWithLocation(token).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryWithLocation(token)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success)
    }
}
