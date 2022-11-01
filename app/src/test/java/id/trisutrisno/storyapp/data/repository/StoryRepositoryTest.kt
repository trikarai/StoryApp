package id.trisutrisno.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.trisutrisno.storyapp.MainDispatcherRule
import id.trisutrisno.storyapp.data.FakeStoryDao
import id.trisutrisno.storyapp.data.FakeStoryDatabase
import id.trisutrisno.storyapp.data.FakeStoryService
import id.trisutrisno.storyapp.data.local.dao.StoryDao
import id.trisutrisno.storyapp.data.local.database.StoryDatabase
import id.trisutrisno.storyapp.data.remote.api.ApiService
import id.trisutrisno.storyapp.repository.StoryRepository
import id.trisutrisno.storyapp.utils.DataDummy
import id.trisutrisno.storyapp.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import id.trisutrisno.storyapp.utils.Result

@ExperimentalCoroutinesApi
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var storyService: ApiService
    private lateinit var storyDao: StoryDao
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyDatabase: StoryDatabase

    private val dummyToken = "token"
    private val dummyFalseToken = "false_token"
    private val dummyStories = DataDummy.generateDummyStoriesResponse()
    private val dummyEmptyStories = DataDummy.generateDummyEmptyStoriesResponse()
    private val dummyPostStoryResponse = DataDummy.generateDummyPostStoryResponse()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()
    private val dummyLatitude = DataDummy.generateDummyRequestBody()
    private val dummyLongitude = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        storyService = FakeStoryService()
        storyDao = FakeStoryDao()
        storyDatabase = FakeStoryDatabase()
        storyRepository = StoryRepository(storyService, storyDatabase)
    }

    @Test
    fun `Get stories should not null`() = runTest {
        val actualResponse = storyRepository.fetchAllStory(dummyToken)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
        }
    }

    @Test
    fun `Get stories with location should not null`() = runTest {
        val expectedResponse = dummyStories
        val actualResponse = storyRepository.fetchAllStoryWithLocation(dummyToken)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertNotNull(actualResponse.value?.data?.forEach {
                it.lon
            })
            Assert.assertNotNull(actualResponse.value?.data?.forEach {
                it.lat
            })
            Assert.assertEquals(expectedResponse.listStory.size, actualResponse.value?.data?.size)
        }
    }

    @Test
    fun `Get empty stories with empty result`() = runTest {
        val expectedResponse = dummyEmptyStories
        val actualResponse = storyRepository.fetchAllStoryWithLocation(dummyFalseToken)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(expectedResponse.listStory.size, actualResponse.value?.data?.size)
        }
    }

    @Test
    fun `Upload story with success result`() = runTest {
        val expectedResponse = dummyPostStoryResponse

        val actualResponse = storyRepository.uploadStory(
            dummyToken, dummyMultipart, dummyDescription, dummyLatitude, dummyLongitude
        )

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(expectedResponse, (actualResponse.value as Result.Success).data)
        }
    }
}