package id.trisutrisno.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.trisutrisno.storyapp.data.local.dao.StoryDao
import id.trisutrisno.storyapp.data.local.entity.StoryEntity

class FakeStoryDao : StoryDao {
    private var stories = mutableListOf<StoryEntity>()

    override fun insertStory(storyEntity: StoryEntity) {
        stories.add(storyEntity)
    }

    override fun fetchAllStories(): PagingSource<Int, StoryEntity> {
        return FakePagingSource(stories)
    }

    override fun deleteAll() {
        stories.clear()
    }

    class FakePagingSource(private val data: MutableList<StoryEntity>) :
        PagingSource<Int, StoryEntity>() {
        @Suppress("SameReturnValue")
        override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int = 0

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
            return LoadResult.Page(data, 0, 1)
        }

    }
}