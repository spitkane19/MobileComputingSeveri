package com.example.mobilecomputingseveri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class database(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "User_Name") val UserName: String?,
    @ColumnInfo(name = "pictureData") val pictureData: String?

)
