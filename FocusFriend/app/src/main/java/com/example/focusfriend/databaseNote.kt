package com.example.focusfriend

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [noteEntity::class], version = 1)
abstract class databaseNote:RoomDatabase() {
    abstract fun noteDao():noteDao
}