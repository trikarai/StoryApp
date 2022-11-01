package id.trisutrisno.storyapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.trisutrisno.storyapp.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(storyEntity: StoryEntity)

    @Query("SELECT * FROM story_db")
    fun fetchAllStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story_db")
    fun deleteAll()
}