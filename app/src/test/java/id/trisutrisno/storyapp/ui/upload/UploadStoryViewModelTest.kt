package id.trisutrisno.storyapp.ui.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import id.trisutrisno.storyapp.data.remote.response.GeneralResponse
import id.trisutrisno.storyapp.repository.StoryRepository
import id.trisutrisno.storyapp.utils.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import id.trisutrisno.storyapp.utils.Result
import id.trisutrisno.storyapp.utils.getOrAwaitValue

@RunWith(MockitoJUnitRunner::class)
class UploadStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var uploadStoryViewModel: UploadStoryViewModel

    private val dummyToken = "token"
    private val dummyStoryAddResponse = DataDummy.generateDummyGeneralResponse()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()
    private val dummyLatitude = DataDummy.generateDummyRequestBody()
    private val dummyLongitude = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        uploadStoryViewModel = UploadStoryViewModel(storyRepository)
    }

    @Test
    fun `Upload file success with share location`() {
        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Success(dummyStoryAddResponse)

        `when`(
            storyRepository.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            )
        ).thenReturn(expectedResponse)

        val actualResponse =
            uploadStoryViewModel.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            ).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        )

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success<*>)
        Assert.assertEquals(
            dummyStoryAddResponse,
            (actualResponse as Result.Success<*>).data
        )
    }

    @Test
    fun `Upload file failed with share location`() {
        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(
            storyRepository.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            )
        ).thenReturn(expectedResponse)

        val actualResponse = uploadStoryViewModel.uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        )

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }

    @Test
    fun `Upload file success without share location`() {
        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Success(dummyStoryAddResponse)

        `when`(
            storyRepository.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(expectedResponse)

        val actualResponse =
            uploadStoryViewModel.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                null,
                null
            ).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            null,
            null
        )

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(
            dummyStoryAddResponse,
            (actualResponse as Result.Success).data
        )
    }

    @Test
    fun `Upload file failed without share location`() {
        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(
            storyRepository.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(expectedResponse)

        val actualResponse = uploadStoryViewModel.uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            null,
            null
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            null,
            null
        )

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }

}