package com.example.nancost.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.nancost.model.Nancost
import kotlinx.coroutines.flow.Flow

@Dao
interface NancostDao {
    @Query("SELECT * FROM nancost_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Nancost>>

    @Query("SELECT * FROM nancost_table WHERE nancost_name = :name")
    suspend fun getByName(name: String): Nancost

    @Insert
    suspend fun add(nancost: Nancost)

    @Update
    suspend fun update(input: Nancost)

    @Query("DELETE FROM nancost_table WHERE id = :id")
    suspend fun delete(id: Int)
}