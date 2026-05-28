package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val db: AppDatabase) {
    val enquiries: Flow<List<Enquiry>> = db.enquiryDao().getAllEnquiries()
    val payments: Flow<List<Payment>> = db.paymentDao().getAllPayments()
    
    // CMS Observables
    val cmsConfigs: Flow<List<CmsConfig>> = db.cmsDao().getAllConfigs()
    val cmsServices: Flow<List<CmsService>> = db.cmsDao().getAllServices()
    val cmsSlides: Flow<List<CmsSlide>> = db.cmsDao().getAllSlides()

    suspend fun insertEnquiry(enquiry: Enquiry): Long {
        return db.enquiryDao().insertEnquiry(enquiry)
    }

    suspend fun updateEnquiry(enquiry: Enquiry) {
        db.enquiryDao().updateEnquiry(enquiry)
    }

    suspend fun deleteEnquiryById(id: Int) {
        db.enquiryDao().deleteEnquiryById(id)
    }

    suspend fun insertPayment(payment: Payment): Long {
        return db.paymentDao().insertPayment(payment)
    }

    // CMS Modifications
    suspend fun insertConfig(config: CmsConfig) {
        db.cmsDao().insertConfig(config)
    }

    suspend fun insertConfigs(configs: List<CmsConfig>) {
        db.cmsDao().insertConfigs(configs)
    }

    suspend fun insertService(service: CmsService) {
        db.cmsDao().insertService(service)
    }

    suspend fun updateService(service: CmsService) {
        db.cmsDao().updateService(service)
    }

    suspend fun deleteServiceById(id: Int) {
        db.cmsDao().deleteServiceById(id)
    }

    suspend fun insertSlide(slide: CmsSlide) {
        db.cmsDao().insertSlide(slide)
    }

    suspend fun updateSlide(slide: CmsSlide) {
        db.cmsDao().updateSlide(slide)
    }

    suspend fun deleteSlideById(id: Int) {
        db.cmsDao().deleteSlideById(id)
    }
}
