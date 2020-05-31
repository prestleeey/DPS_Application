package com.example.dps_application.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dps_application.domain.model.Attachment
import com.example.dps_application.domain.model.Message
import com.example.dps_application.domain.model.User

@Database(
    entities = [Message::class, User::class, Attachment::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DPSDb : RoomDatabase() {
    abstract fun messages(): MessageDao
}