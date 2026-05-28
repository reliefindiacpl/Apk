package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_records")
data class Payment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val paymentType: String, // "Book Floor Plan", "Advance Payment"
    val amount: Double,
    val customerName: String,
    val customerPhone: String,
    val paymentStatus: String, // "Success", "Pending", "Failed"
    val txnId: String,
    val timestamp: Long = System.currentTimeMillis()
)
