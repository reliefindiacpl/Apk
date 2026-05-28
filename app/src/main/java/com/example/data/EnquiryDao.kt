package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EnquiryDao {
    @Query("SELECT * FROM enquiries ORDER BY timestamp DESC")
    fun getAllEnquiries(): Flow<List<Enquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: Enquiry): Long

    @Update
    suspend fun updateEnquiry(enquiry: Enquiry)

    @Delete
    suspend fun deleteEnquiry(enquiry: Enquiry)

    @Query("DELETE FROM enquiries WHERE id = :id")
    suspend fun deleteEnquiryById(id: Int)
}
