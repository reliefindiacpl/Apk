package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enquiries")
data class Enquiry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val plotSize: String,
    val location: String,
    val requirement: String,
    val message: String,
    val status: String = "New", // "New", "Contacted", "Closed"
    val timestamp: Long = System.currentTimeMillis(),
    val followUpDate: Long? = null,
    val adminNotes: String = ""
)
