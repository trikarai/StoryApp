package id.trisutrisno.storyapp.ui.story

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import id.trisutrisno.storyapp.MainDispatcherRule
import id.trisutrisno.storyapp.domain.model.Story
import id.trisutrisno.storyapp.domain.model.toStoryEntity
import id.trisutrisno.storyapp.repository.StoryRepository
import id.trisutrisno.storyapp.repository.UserRepository
import id.trisutrisno.storyapp.ui.story.adapter.StoryAdapter
import id.trisutrisno.storyapp.utils.DataDummy
import id.trisutrisno.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var context: Context

    private val dummyStoriesWithoutLocation = DataDummy.generateDummyStoriesWithoutLocation()
    private val dummyToken = "token"

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyRepository)
        context = Mockito.mock(Context::class.java)
    }

    @Test
    fun `Get stories with success result`() = runTest {
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStoriesWithoutLocation)

        val expectedResponse = MutableLiveData<PagingData<Story>>()
        expectedResponse.value = data

        `when`(storyRepository.fetchAllStory(dummyToken)).thenReturn(expectedResponse.map { result ->
            result.map {
                it.toStoryEntity()
            }
        })

        val storyViewModel = StoryViewModel(storyRepository)
        val actualResponse : PagingData<Story> = storyViewModel.fetchAllStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualResponse)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStoriesWithoutLocation, differ.snapshot())
        Assert.assertEquals(dummyStoriesWithoutLocation.size, differ.snapshot().size)
        Assert.assertEquals(dummyStoriesWithoutLocation[0].name, differ.snapshot()[0]?.name)
    }


}

class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}