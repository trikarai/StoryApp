package id.trisutrisno.storyapp.data

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import id.trisutrisno.storyapp.data.local.dao.RemoteKeysDao
import id.trisutrisno.storyapp.data.local.dao.StoryDao
import id.trisutrisno.storyapp.data.local.database.StoryDatabase
import org.mockito.Mockito

class FakeStoryDatabase : StoryDatabase() {
    override fun storyDao(): StoryDao = FakeStoryDao()

    override fun remoteKeysDao(): RemoteKeysDao = FakeRemoteKeysDao()

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        return Mockito.mock(SupportSQLiteOpenHelper::class.java)
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return Mockito.mock(InvalidationTracker::class.java)
    }

    override fun clearAllTables() {}
}