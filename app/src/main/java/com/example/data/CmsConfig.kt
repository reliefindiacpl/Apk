package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cms_configs")
data class CmsConfig(
    @PrimaryKey val key: String,
    val value: String
)
