package com.project.lalabib.storiesin.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.project.lalabib.storiesin.utils.DataDummy
import com.project.lalabib.storiesin.utils.MainDispatcherRule
import com.project.lalabib.storiesin.data.response.ListStoryItem
import com.project.lalabib.storiesin.utils.getOrAwaitValue
import com.project.lalabib.storiesin.service.ApiService
import com.project.lalabib.storiesin.ui.main.MainViewModel
import com.project.lalabib.storiesin.ui.main.StoryAdapter
import com.project.lalabib.storiesin.ui.main.StoryPagingSource
import com.project.lalabib.storiesin.ui.main.noopListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    private lateinit var apiService: ApiService

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Before
    fun setUp() {
        apiService = FakeApiService()
    }

    @Test
    fun postRegisterTest() = mainDispatcherRules.runBlockingTest {
        val expectedUser = DataDummy.generateDummyRegisterResponse()
        val actualUser = apiService.postRegister(name, email, password)
        Assert.assertNotNull(actualUser)
        Assert.assertEquals(expectedUser.message, actualUser.message)
    }

    @Test
    fun postLoginTest() = mainDispatcherRules.runBlockingTest {
        val expectedUser = DataDummy.generateDummyLoginResponse()
        val actualUser = apiService.postLogin(email, password)
        Assert.assertNotNull(actualUser)
        Assert.assertEquals(expectedUser.message, actualUser.message)
    }

    @Test
    fun getStoriesTest() = mainDispatcherRules.runBlockingTest {
        val dummyStories = DataDummy.generateDummyListStory()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStories)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()

        expectedStory.value = data
        Mockito.`when`(storyRepository.getListStories()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<ListStoryItem> = mainViewModel.getListStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories, differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0].name, differ.snapshot()[0]?.name)
    }

    @Test
    fun getStoriesWithLocationTest() = mainDispatcherRules.runBlockingTest {
        val expectedStory = DataDummy.generateDummyStoriesWithLocResponse()
        val actualStory = apiService.getStory(token, 1, 1, 30)
        Assert.assertNotNull(actualStory)
        Assert.assertEquals(expectedStory.listStory.size, actualStory.listStory.size)
    }

    @Test
    fun uploadTest() = mainDispatcherRules.runBlockingTest {
        val description = "deksripsi".toRequestBody("text/plain".toMediaType())

        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val expectedStory = DataDummy.generateDummyUploadStoryResponse()
        val actualStory = apiService.postStory(token, imageMultipart, description)
        Assert.assertNotNull(actualStory)
        Assert.assertEquals(expectedStory.message, actualStory.message)
    }

    companion object {
        private const val token = "TOKEN"
        private const val name = "Your Name"
        private const val email = "email@gmail.com"
        private const val password = "password"
    }
}