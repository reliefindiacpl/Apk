package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CmsDao {
    // 1. Configs
    @Query("SELECT * FROM cms_configs")
    fun getAllConfigs(): Flow<List<CmsConfig>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: CmsConfig)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfigs(configs: List<CmsConfig>)

    @Query("SELECT value FROM cms_configs WHERE `key` = :key")
    suspend fun getConfigValue(key: String): String?

    // 2. Services
    @Query("SELECT * FROM cms_services ORDER BY id ASC")
    fun getAllServices(): Flow<List<CmsService>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: CmsService)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<CmsService>)

    @Update
    suspend fun updateService(service: CmsService)

    @Delete
    suspend fun deleteService(service: CmsService)

    @Query("DELETE FROM cms_services WHERE id = :id")
    suspend fun deleteServiceById(id: Int)

    // 3. Slides (Banners)
    @Query("SELECT * FROM cms_slides ORDER BY id ASC")
    fun getAllSlides(): Flow<List<CmsSlide>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlide(slide: CmsSlide)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlides(slides: List<CmsSlide>)

    @Update
    suspend fun updateSlide(slide: CmsSlide)

    @Delete
    suspend fun deleteSlide(slide: CmsSlide)

    @Query("DELETE FROM cms_slides WHERE id = :id")
    suspend fun deleteSlideById(id: Int)
}
