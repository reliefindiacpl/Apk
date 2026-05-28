package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cms_slides")
data class CmsSlide(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val desc: String,
    val btnText: String,
    val destination: String, // "Payment", "Enquiry", "Services"
    val categoryType: String // "design", "construction", "interior", "structural", "material"
)
