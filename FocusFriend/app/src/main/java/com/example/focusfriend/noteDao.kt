package com.example.focusfriend

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface noteDao {
    @Insert
    suspend fun insert(todo:noteEntity)
    @Delete
    suspend fun delete(todo: noteEntity)
    @Query("DELETE FROM todo WHERE id=:id")
    suspend fun deleteOne(id:Long)
    @Query("SELECT * FROM todo")
    fun showTodo():LiveData<List<noteEntity>>
}