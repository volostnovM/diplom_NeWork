package ru.netology.neworknetology.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.neworknetology.entity.JobEntity

@Dao
interface JobDao {
    @Query("SELECT * FROM JobEntity ORDER BY id DESC")
    fun getAllJob(): Flow<List<JobEntity>>
    @Query("SELECT * FROM JobEntity WHERE id = :jobId LIMIT 1")
    suspend fun getById(jobId: Int): JobEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: JobEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(jobs: List<JobEntity>)
    @Query("DELETE FROM JobEntity WHERE id = :id")
    suspend fun removeById(id: Int)
    @Query("DELETE FROM JobEntity")
    suspend fun removeAll()
}