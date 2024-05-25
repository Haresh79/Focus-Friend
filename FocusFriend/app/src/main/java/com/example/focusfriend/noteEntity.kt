package com.example.focusfriend

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class noteEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val text:String
)
