package id.trisutrisno.storyapp.data.local.room
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKeyModel: List<RemoteKeysModel>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    fun getRemoteKeysId(id: String): RemoteKeysModel?

    @Query("DELETE FROM remote_keys")
    fun deleteRemoteKeys()
}