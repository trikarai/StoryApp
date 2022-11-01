package id.trisutrisno.storyapp.data

import id.trisutrisno.storyapp.data.local.dao.RemoteKeysDao
import id.trisutrisno.storyapp.data.local.entity.RemoteKeysEntity

class FakeRemoteKeysDao : RemoteKeysDao {
    private val remoteKeys = mutableListOf<RemoteKeysEntity>()

    override suspend fun insertAll(remoteKey: List<RemoteKeysEntity>) {
        remoteKeys.addAll(remoteKey)
    }

    override suspend fun getRemoteKeysId(id: String): RemoteKeysEntity? {
        return remoteKeys.firstOrNull { it.id == id }
    }

    override suspend fun deleteRemoteKeys() {
        remoteKeys.clear()
    }
}