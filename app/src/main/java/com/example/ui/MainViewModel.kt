package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Services : Screen()
    data class EnquiryForm(val selectedService: String? = null) : Screen()
    object Contact : Screen()
    object PaymentScreen : Screen()
    object AdminLogin : Screen()
    object AdminDashboard : Screen()
}

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    // CMS Observables
    val cmsConfigs: StateFlow<List<CmsConfig>> = repository.cmsConfigs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cmsServices: StateFlow<List<CmsService>> = repository.cmsServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cmsSlides: StateFlow<List<CmsSlide>> = repository.cmsSlides
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cmsConfigMap: StateFlow<Map<String, String>> = repository.cmsConfigs
        .map { list -> list.associate { it.key to it.value } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    init {
        viewModelScope.launch {
            try {
                val existingConfigs = repository.cmsConfigs.first()
                if (existingConfigs.isEmpty()) {
                    initializeDefaultConfigs()
                }
            } catch (e: Exception) {
                // Ignore initialization errors
            }
            try {
                val existingServices = repository.cmsServices.first()
                if (existingServices.isEmpty()) {
                    initializeDefaultServices()
                }
            } catch (e: Exception) {
                // Ignore
            }
            try {
                val existingSlides = repository.cmsSlides.first()
                if (existingSlides.isEmpty()) {
                    initializeDefaultSlides()
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private suspend fun initializeDefaultConfigs() {
        val defaults = listOf(
            CmsConfig("show_banners_slider", "true"),
            CmsConfig("show_quick_actions", "true"),
            CmsConfig("show_why_choose_us", "true"),
            CmsConfig("show_testimonials", "true"),
            CmsConfig("show_alert_banner", "true"),
            CmsConfig("contact_phone", "+91 94500 56832"),
            CmsConfig("contact_email", "reliefindiacpl@gmail.com"),
            CmsConfig("contact_whatsapp", "+919450056832"),
            CmsConfig("contact_address", "Near National Highway 29, Barhalganj, Gorakhpur, UP, India"),
            CmsConfig("pricing_floor_plan_fee", "4999.0"),
            CmsConfig("promo_alert_text", "Monsoon Offer: Book any House architectural design this week & get free plumbing maps!")
        )
        repository.insertConfigs(defaults)
    }

    private suspend fun initializeDefaultServices() {
        val defaults = listOf(
            CmsService(
                title = "House Planning & 2D/3D Design",
                desc = "Premium CAD layouts, blueprint drafting, furniture mapping, exterior elevations, and heavy architectural walkthrough rendering.",
                pointsCsv = "Detailed 2D Blueprints,Dynamic 3D Elevations,Vastu-Compliant Mapping",
                categoryType = "design"
            ),
            CmsService(
                title = "Construction (Turnkey Projects)",
                desc = "Full foundation-to-paint engineering contracts. We provide certified structural pillars, premium bricks, heavy concrete, and quality labour.",
                pointsCsv = "High Grade Cement & Steel,Experienced Civil Crews,Chronological Milestones",
                categoryType = "construction"
            ),
            CmsService(
                title = "Interior & Exterior Design",
                desc = "Sleek living spaces, luxury modular kitchens, durable landscaping, designer wall finishings, and high-tech ceiling units.",
                pointsCsv = "Acrylic Modular Kitchens,Contemporary TV Cabinets,Outdoor Architectural Landscapes",
                categoryType = "interior"
            ),
            CmsService(
                title = "Electrical, Plumbing & HVAC",
                desc = "Expert internal cabling, concealed pipe lines, safe electric switchboards, premium bathrooms layouts, and centralized cooling duct routing.",
                pointsCsv = "Concealed Wire Layouts,Leak-Proof CPVC Plumbing,Integrated Cool Air Shafts",
                categoryType = "electrical"
            ),
            CmsService(
                title = "Structural Design & Approval",
                desc = "Seismic resistant column detailing, load-bearing soil analysis, structural safety certification, and municipal map approval.",
                pointsCsv = "Seismic Safe Columns,Soil Load Reports,Legal Authority Verifications",
                categoryType = "structural"
            ),
            CmsService(
                title = "Material Supply",
                desc = "High-grade sand supply, premium structural aggregate blocks, cement deliveries, and customized reinforcing steel bars.",
                pointsCsv = "Prompt Truck Transport,Bulk Supply Discounts,A-Grade Red Bricks",
                categoryType = "material"
            )
        )
        for (service in defaults) {
            repository.insertService(service)
        }
    }

    private suspend fun initializeDefaultSlides() {
        val defaults = listOf(
            CmsSlide(
                title = "Book Premium Floor Plans",
                desc = "Get custom 2D layouts and 3D architectural elevations drafted in 48 hours.",
                btnText = "Book Now (₹4,999)",
                destination = "Payment",
                categoryType = "design"
            ),
            CmsSlide(
                title = "Civil Turnkey Execution",
                desc = "A complete engineering design-and-make construction service at optimal rates.",
                btnText = "Get Free Quote",
                destination = "Enquiry",
                categoryType = "construction"
            ),
            CmsSlide(
                title = "Luxurious Interior & Exterior",
                desc = "Sophisticated modular kitchens, premium spatial aesthetics, and landscaping.",
                btnText = "View Gallery",
                destination = "Services",
                categoryType = "interior"
            ),
            CmsSlide(
                title = "Approved Beams & Structures",
                desc = "Seismic-resistant, heavyload structures approved by municipal authorities.",
                btnText = "Request Structural Approval",
                destination = "Enquiry",
                categoryType = "structural"
            )
        )
        for (slide in defaults) {
            repository.insertSlide(slide)
        }
    }

    // State-driven routing stack
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val backStack = mutableListOf<Screen>()

    fun navigateTo(screen: Screen) {
        // Prevent adding duplicate screens to the backstack
        if (_currentScreen.value != screen) {
            backStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (backStack.isNotEmpty()) {
            _currentScreen.value = backStack.removeAt(backStack.size - 1)
            return true
        }
        return false
    }

    // Room Database Observables
    val enquiries: StateFlow<List<Enquiry>> = repository.enquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val payments: StateFlow<List<Payment>> = repository.payments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin login states
    private val _adminLoggedIn = MutableStateFlow(false)
    val adminLoggedIn: StateFlow<Boolean> = _adminLoggedIn.asStateFlow()

    fun adminLogin(username: String, password: String): Boolean {
        if (username.trim().lowercase() == "admin" && password == "reliefindia123") {
            _adminLoggedIn.value = true
            navigateTo(Screen.AdminDashboard)
            return true
        }
        return false
    }

    fun adminLogout() {
        _adminLoggedIn.value = false
        backStack.clear()
        _currentScreen.value = Screen.Home
    }

    // Simulated alerts / Push Notification States
    private val _activeNotification = MutableStateFlow<String?>(null)
    val activeNotification: StateFlow<String?> = _activeNotification.asStateFlow()

    fun clearNotification() {
        _activeNotification.value = null
    }

    // Enquiry Lead Form Inputs
    val leadName = MutableStateFlow("")
    val leadPhone = MutableStateFlow("")
    val leadPlotSize = MutableStateFlow("")
    val leadLocation = MutableStateFlow("")
    val leadRequirement = MutableStateFlow("")
    val leadMessage = MutableStateFlow("")

    fun submitEnquiry(onSuccess: () -> Unit) {
        val name = leadName.value.trim()
        val phone = leadPhone.value.trim()
        val plotSize = leadPlotSize.value.trim()
        val location = leadLocation.value.trim()
        val requirement = leadRequirement.value.trim()
        val message = leadMessage.value.trim()

        if (name.isEmpty() || phone.isEmpty() || requirement.isEmpty()) {
            return
        }

        val designEnquiry = Enquiry(
            name = name,
            phone = phone,
            plotSize = if (plotSize.isEmpty()) "Not specified" else plotSize,
            location = if (location.isEmpty()) "Not specified" else location,
            requirement = requirement,
            message = if (message.isEmpty()) "Interested in $requirement services." else message
        )

        viewModelScope.launch {
            repository.insertEnquiry(designEnquiry)
            _activeNotification.value = "New Lead Alert: $name requested $requirement!"
            clearForm()
            onSuccess()
        }
    }

    fun clearForm() {
        leadName.value = ""
        leadPhone.value = ""
        leadPlotSize.value = ""
        leadLocation.value = ""
        leadRequirement.value = ""
        leadMessage.value = ""
    }

    // Lead adjustments for Admin usage
    fun updateLeadStatus(enquiry: Enquiry, newStatus: String) {
        viewModelScope.launch {
            repository.updateEnquiry(enquiry.copy(status = newStatus))
        }
    }

    fun updateLeadNotes(enquiry: Enquiry, notes: String) {
        viewModelScope.launch {
            repository.updateEnquiry(enquiry.copy(adminNotes = notes))
        }
    }

    fun updateLeadFollowUp(enquiry: Enquiry, dateTimeMillis: Long?) {
        viewModelScope.launch {
            repository.updateEnquiry(enquiry.copy(followUpDate = dateTimeMillis))
        }
    }

    fun deleteLead(enquiryId: Int) {
        viewModelScope.launch {
            repository.deleteEnquiryById(enquiryId)
        }
    }

    // Modern payment builder
    fun executePayment(
        type: String, // "Book Floor Plan", "Advance Payment"
        amount: Double,
        name: String,
        phone: String,
        onSuccess: (Payment) -> Unit
    ) {
        val txnId = "RI${(10000000..99999999).random()}"
        val record = Payment(
            paymentType = type,
            amount = amount,
            customerName = name,
            customerPhone = phone,
            paymentStatus = "Success",
            txnId = txnId
        )
        viewModelScope.launch {
            repository.insertPayment(record)
            _activeNotification.value = "Payment of ₹${String.format("%,.0f", amount)} received from $name!"
            onSuccess(record)
        }
    }

    // CMS Mutations
    fun updateCmsConfigValue(key: String, value: String) {
        viewModelScope.launch {
            repository.insertConfig(CmsConfig(key, value))
        }
    }

    fun addCmsService(title: String, desc: String, pointsCsv: String, categoryType: String) {
        viewModelScope.launch {
            repository.insertService(CmsService(title = title, desc = desc, pointsCsv = pointsCsv, categoryType = categoryType))
        }
    }

    fun updateCmsService(service: CmsService) {
        viewModelScope.launch {
            repository.updateService(service)
        }
    }

    fun deleteCmsService(serviceId: Int) {
        viewModelScope.launch {
            repository.deleteServiceById(serviceId)
        }
    }

    fun addCmsSlide(title: String, desc: String, btnText: String, destination: String, categoryType: String) {
        viewModelScope.launch {
            repository.insertSlide(CmsSlide(title = title, desc = desc, btnText = btnText, destination = destination, categoryType = categoryType))
        }
    }

    fun updateCmsSlide(slide: CmsSlide) {
        viewModelScope.launch {
            repository.updateSlide(slide)
        }
    }

    fun deleteCmsSlide(slideId: Int) {
        viewModelScope.launch {
            repository.deleteSlideById(slideId)
        }
    }
}
