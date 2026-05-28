package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cms_services")
data class CmsService(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val desc: String,
    val pointsCsv: String, // comma-separated list of points (e.g. "Detailed 2D Blueprints, Vastu Correct, Dynamic Elevation")
    val categoryType: String // e.g. "design", "construction", "interior", "electrical", "structural", "material"
)
