package id.trisutrisno.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.trisutrisno.storyapp.data.local.entity.RemoteKeysEntity
import id.trisutrisno.storyapp.data.local.database.StoryDatabase
import id.trisutrisno.storyapp.data.local.entity.StoryEntity
import id.trisutrisno.storyapp.data.remote.api.ApiService
import java.lang.Exception

@ExperimentalPagingApi
class StoryRemoteMediator(
    private val token: String,
    private val api: ApiService,
    private val database: StoryDatabase
) : RemoteMediator<Int, StoryEntity>() {


    override suspend fun load(loadType: LoadType, state: PagingState<Int, StoryEntity>): MediatorResult {
        try {
            val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
          }

            val responseData = api.fetchStories("Bearer $token", page, state.config.pageSize)

            val endOfPaginationReached = responseData.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page -1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.listStory.map {
                    RemoteKeysEntity(it.id, prevKey, nextKey)
                }

                database.remoteKeysDao().insertAll(keys)

                responseData.listStory.forEach { listStoryItem ->
                    val storyData = StoryEntity(
                        listStoryItem.id,
                        listStoryItem.name,
                        listStoryItem.description,
                        listStoryItem.createdAt,
                        listStoryItem.photoUrl,
                        listStoryItem.lon,
                        listStoryItem.lat
                    )
                    database.storyDao().insertStory(storyData)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }


    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}