package com.example.mobilecomputingseveri
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class ProfileDatabase(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "userName") val user_Name: String?,
    @ColumnInfo(name = "pictureData") val picture_Data: String

)
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileDatabase)

    @Query("SELECT * FROM ProfileDatabase WHERE userName = :userName")
    suspend fun getProfileByUserName(userName: String): ProfileDatabase?

    @Query("SELECT * FROM ProfileDatabase WHERE pictureData = :pictureData")
    suspend fun getProfileByPictureData(pictureData: String): ProfileDatabase?

    @Query("UPDATE ProfileDatabase SET userName = :newUserName WHERE id = :profileId")
    suspend fun updateUserName(profileId: Int, newUserName: String)

    @Query("UPDATE ProfileDatabase SET pictureData = :newPictureData WHERE id = :profileId")
    suspend fun updatePictureData(profileId: Int, newPictureData: String)

    @Query("SELECT pictureData FROM ProfileDatabase WHERE id = :profileId")
    suspend fun getPictureDataById(profileId: Int): String

    @Query("SELECT userName FROM ProfileDatabase WHERE id = :profileId")
    suspend fun getuserNameById(profileId: Int): String

    suspend fun ensureInitialEntityExists() {
        val profile = getProfileById(1)
        if (profile == null) {
            insertProfile(
                ProfileDatabase(
                    id = 1,
                    user_Name = "DefaultUser",
                    picture_Data = ""
                )
            )
        }
    }

    @Query("SELECT * FROM ProfileDatabase WHERE id = :profileId")
    suspend fun getProfileById(profileId: Int): ProfileDatabase?
}


