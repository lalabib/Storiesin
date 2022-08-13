package com.project.lalabib.storiesin.ui.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.project.lalabib.storiesin.utils.DataDummy
import com.project.lalabib.storiesin.data.Result
import com.project.lalabib.storiesin.data.StoryRepository
import com.project.lalabib.storiesin.data.response.AddStoryResponse
import com.project.lalabib.storiesin.utils.getOrAwaitValue
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

@RunWith(MockitoJUnitRunner::class)
class UploadViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var uploadStoryViewModel: UploadViewModel
    private val dummyUploadStory = DataDummy.generateDummyUploadStoryResponse()
    private val token = "TOKEN"

    @Before
    fun setUp() {
        uploadStoryViewModel = UploadViewModel(storyRepository)
    }

    @Test
    fun `when Upload Story is Success`() {
        val description = "deksripsi".toRequestBody("text/plain".toMediaType())
        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val expectedStory = MutableLiveData<Result<AddStoryResponse>>()
        expectedStory.value = Result.Success(dummyUploadStory)
        Mockito.`when`(uploadStoryViewModel.postStory(token, imageMultipart, description)).thenReturn(expectedStory)

        val actualStory = uploadStoryViewModel.postStory(token, imageMultipart, description).getOrAwaitValue()

        Mockito.verify(storyRepository).postStory(token, imageMultipart, description)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success)
    }
}