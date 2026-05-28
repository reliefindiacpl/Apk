package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Master layout for our construction app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerLayout(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeNotif by viewModel.activeNotification.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Real-time Top push notifications bar simulation
    LaunchedEffect(activeNotif) {
        if (activeNotif != null) {
            delay(5000)
            viewModel.clearNotification()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CompanyLogoDrawing(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF)) // light blue background
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "RELIEF INDIA",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VibrantBlue,
                                    letterSpacing = 2.5.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                                Text(
                                    text = "Construction Pvt. Ltd.",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = TextPrimary,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = TextPrimary
                    ),
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.navigateTo(Screen.AdminLogin)
                            },
                            modifier = Modifier
                                .testTag("admin_login_lock_button")
                                .padding(end = 8.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEFF6FF)) // blue-50 equivalent
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Developer Admin Portal Login",
                                tint = VibrantBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )
                HorizontalDivider(color = SlateBorder, thickness = 1.dp)
                
                // Animated Banner Alert (Simulating In-App / Push Notification Alert)
                AnimatedVisibility(
                    visible = activeNotif != null,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    activeNotif?.let { notif ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notification alert",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = notif,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.clearNotification() },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close local alert notification",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = SlateBorder, thickness = 1.dp)
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    val items = listOf(
                        NavigationItem("Home", Icons.Default.Home, Screen.Home, "nav_home"),
                        NavigationItem("Services", Icons.Default.List, Screen.Services, "nav_services"),
                        NavigationItem("Enquiry", Icons.Default.Edit, Screen.EnquiryForm(), "nav_enquiry"),
                        NavigationItem("Payments", Icons.Default.ShoppingCart, Screen.PaymentScreen, "nav_payments"),
                        NavigationItem("Contact", Icons.Default.Call, Screen.Contact, "nav_contact")
                    )

                    items.forEach { item ->
                        val isSelected = when (currentScreen) {
                            is Screen.Home -> item.screen is Screen.Home
                            is Screen.Services -> item.screen is Screen.Services
                            is Screen.EnquiryForm -> item.screen is Screen.EnquiryForm
                            is Screen.PaymentScreen -> item.screen is Screen.PaymentScreen
                            is Screen.Contact -> item.screen is Screen.Contact
                            else -> false
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(item.screen) },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VibrantBlue,
                                selectedTextColor = VibrantBlue,
                                indicatorColor = Color(0xFFEFF6FF), // elegant soft blue circle background
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.testTag(item.tag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentScreen) {
                is Screen.Home -> HomeScreen(viewModel)
                is Screen.Services -> ServicesScreen(viewModel)
                is Screen.EnquiryForm -> {
                    val selectedService = (currentScreen as Screen.EnquiryForm).selectedService
                    EnquiryFormScreen(viewModel, selectedService)
                }
                is Screen.PaymentScreen -> PaymentScreen(viewModel)
                is Screen.Contact -> ContactScreen(viewModel)
                else -> {
                    // Fallback to home, other screens (like Admin login and panel) handled by MainActivity
                    HomeScreen(viewModel)
                }
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen,
    val tag: String
)

// === 1. HOME SCREEN COMPOSABLE ===
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val configs by viewModel.cmsConfigMap.collectAsState()

    val showBanners = configs["show_banners_slider"] ?: "true"
    val showQuickActions = configs["show_quick_actions"] ?: "true"
    val showWhyUs = configs["show_why_choose_us"] ?: "true"
    val showTestimonials = configs["show_testimonials"] ?: "true"
    val showAlertBanner = configs["show_alert_banner"] ?: "true"
    val promoAlertText = configs["promo_alert_text"] ?: "Monsoon Offer: Book any House architectural design this week & get free plumbing maps!"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Hero / Header Branding with Tagline - Editorial Aesthetic Overlapping Theme
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    // Top-right overlapping circle
                    drawCircle(
                        color = Color(0xFF2563EB).copy(alpha = 0.25f),
                        radius = 320f,
                        center = androidx.compose.ui.geometry.Offset(this.size.width * 0.95f, this.size.height * 0.15f)
                    )
                }
                .background(CorporateBlue) // bg-blue-900 look
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "OUR PROMISE",
                    color = Color(0xFF93C5FD), // text-blue-200 equivalent
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = "Complete\nConstruction\n& Design",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    lineHeight = 38.sp,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { viewModel.navigateTo(Screen.Services) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = CorporateBlue
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Explore Services",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    // Tagline details
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Complete Solutions",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }

        // Active Alert Banner
        if (showAlertBanner == "true") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF3C7))
                    .border(BorderStroke(1.dp, Color(0xFFFDE68A)))
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Offer Promo Alert",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = promoAlertText,
                        color = Color(0xFF92400E),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Banners Auto-Slider
        if (showBanners == "true") {
            Text(
                text = "Premium Specialized Solutions",
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 10.dp),
                color = TextPrimary
            )
            BannersSliderSection(viewModel)
        }

        // Quick Actions Grid (Real Interactive Buttons)
        if (showQuickActions == "true") {
            Text(
                text = "Services Quick Actions",
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, top = 22.dp, bottom = 12.dp),
                color = TextPrimary
            )
            QuickActionsGrid(viewModel)
        }

        // "Why Relief India" Section
        if (showWhyUs == "true") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, SlateBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Why Choose Relief India?",
                        fontSize = 17.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    WhyPointItem(Icons.Default.Check, "Turnkey Construction", "We handle design, materials, and labor from foundation to final paint.")
                    WhyPointItem(Icons.Default.Check, "Certified Engineers", "All structures are designed and verified by licensed experts.")
                    WhyPointItem(Icons.Default.Check, "Transparent Accounting", "Zero hidden expenses. Secure digital escrow payment options.")
                }
            }
        }

        // Testimonials
        if (showTestimonials == "true") {
            Text(
                text = "What Our Customers Say",
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
                color = TextPrimary
            )
            TestimonialCarousel()
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun WhyPointItem(icon: ImageVector, title: String, subtitle: String) {
    Row(modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
                .padding(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
        }
    }
}

// Banners auto-slider displaying architectural drawings
@Composable
fun BannersSliderSection(viewModel: MainViewModel) {
    val dbSlides by viewModel.cmsSlides.collectAsState()
    var activePage by remember { mutableStateOf(0) }

    // Automatic slide changes
    LaunchedEffect(dbSlides.size) {
        if (dbSlides.isNotEmpty()) {
            while (true) {
                delay(4000)
                activePage = (activePage + 1) % dbSlides.size
            }
        }
    }

    if (dbSlides.isEmpty()) {
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            val slide = dbSlides.getOrNull(activePage) ?: return@Box
            
            // Layout of the slide
            Row(modifier = Modifier.fillMaxSize()) {
                // Interactive illustration canvas side
                Box(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight()
                ) {
                    when (slide.categoryType) {
                        "design" -> BlueprintDraftingIllustration(modifier = Modifier.fillMaxSize())
                        "construction" -> ConstructionSiteIllustration(modifier = Modifier.fillMaxSize())
                        "interior" -> InteriorExteriorDesignIllustration(modifier = Modifier.fillMaxSize())
                        "structural" -> StructuralDraftingIllustration(modifier = Modifier.fillMaxSize())
                        else -> MaterialSupplyIllustration(modifier = Modifier.fillMaxSize())
                    }
                }
                
                // Details text side
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .background(Color.White)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = slide.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = slide.desc,
                            fontSize = 11.sp,
                            color = Color.DarkGray,
                            lineHeight = 15.sp,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Button(
                        onClick = {
                            when (slide.destination) {
                                "Payment" -> viewModel.navigateTo(Screen.PaymentScreen)
                                "Services" -> viewModel.navigateTo(Screen.Services)
                                "Enquiry" -> viewModel.navigateTo(Screen.EnquiryForm())
                                else -> viewModel.navigateTo(Screen.EnquiryForm(slide.title))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = slide.btnText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))
        
        // Circular indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            dbSlides.forEachIndexed { i, _ ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (i == activePage) 10.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (i == activePage) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f))
                        .clickable { activePage = i }
                )
            }
        }
    }
}

data class SlideData(
    val title: String,
    val desc: String,
    val btnText: String,
    val onClick: () -> Unit,
    val canvasElement: @Composable () -> Unit
)

@Composable
fun QuickActionsGrid(viewModel: MainViewModel) {
    val context = LocalContext.current
    val configs by viewModel.cmsConfigMap.collectAsState()

    val floorPlanFee = configs["pricing_floor_plan_fee"] ?: "4999"
    val showPhone = configs["contact_phone"] ?: "+91-8574367610"
    val waPhone = configs["contact_whatsapp"] ?: "918574367610"
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickButtonCard(
                title = "Book Floor Plan",
                subtitle = "₹$floorPlanFee Standard Fees",
                icon = Icons.Default.ShoppingCart,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(1f)
                    .testTag("action_book_floor_plan"),
                onClick = { viewModel.navigateTo(Screen.PaymentScreen) }
            )
            QuickButtonCard(
                title = "Get Free Quote",
                subtitle = "Fast Lead Builder",
                icon = Icons.Default.Edit,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .weight(1f)
                    .testTag("action_get_quote"),
                onClick = { viewModel.navigateTo(Screen.EnquiryForm()) }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickButtonCard(
                title = "Contact Support",
                subtitle = showPhone,
                icon = Icons.Default.Call,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .weight(1f)
                    .testTag("action_contact_now"),
                onClick = { viewModel.navigateTo(Screen.Contact) }
            )
            QuickButtonCard(
                title = "WhatsApp Helpline",
                subtitle = "Instant Chat Support",
                icon = Icons.Default.Send,
                color = Color(0xFF25D366),
                modifier = Modifier
                    .weight(1f)
                    .testTag("action_whatsapp_chat"),
                onClick = {
                    triggerWhatsAppIntent(
                        context = context,
                        phone = waPhone,
                        message = "Hello Relief India Construction, I am interested in building a new structure and would like to discuss my project requirements."
                    )
                }
            )
        }
    }
}

@Composable
fun QuickButtonCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(78.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun TestimonialCarousel() {
    val testimonials = listOf(
        ReviewData("Ramesh Singh", "Barhalganj, Gorakhpur", "Relief India constructed my 3-story house in Barhalganj. Superb work quality and amazing engineers! Transparent pricing.", 5),
        ReviewData("Sneha Tripathi", "Patna Chauraha", "They delivered my 3D modular kitchen and interior layout on time. Incredible space-saving design solutions!", 5),
        ReviewData("Vikas Maurya", "Gorakhpur Bypass", "Excellent turnkey construction. Highly skilled plumbing and electrical crew. Best structural engineers in Uttar Pradesh.", 5),
        ReviewData("Anand Prasad", "Mukhtipath Road", "Got my house maps and local authority approval in a week. Smooth and legally compliant process. Five stars!", 5)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(testimonials) { review ->
            Card(
                modifier = Modifier
                    .width(260.dp)
                    .height(130.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = review.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• ${review.location}",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${review.reviewText}\"",
                            fontSize = 11.sp,
                            color = Color.DarkGray,
                            lineHeight = 14.sp,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row {
                        repeat(review.rating) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star rating element",
                                tint = Color(0xFFFBC02D),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ReviewData(
    val name: String,
    val location: String,
    val reviewText: String,
    val rating: Int
)

// === 2. SERVICES SECTION COMPOSABLE ===
@Composable
fun ServicesScreen(viewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    val services by viewModel.cmsServices.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 18.dp, horizontal = 20.dp)
        ) {
            Text(
                text = "ENGINEERED SOLUTIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantBlue,
                letterSpacing = 1.6.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "Premium Services",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = TextPrimary
            )
            Text(
                text = "Complete turnkey execution, modular architectural elevations, and certified civil designs.",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        HorizontalDivider(color = SlateBorder, thickness = 1.dp)

        Spacer(modifier = Modifier.height(14.dp))

        services.forEach { service ->
            val points = service.pointsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .testTag("service_card_${service.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    // Title
                    Text(
                        text = service.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Visual illustration Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color.White)
                    ) {
                        when (service.categoryType) {
                            "design" -> BlueprintDraftingIllustration(modifier = Modifier.fillMaxSize())
                            "construction" -> ConstructionSiteIllustration(modifier = Modifier.fillMaxSize())
                            "interior" -> InteriorExteriorDesignIllustration(modifier = Modifier.fillMaxSize())
                            "structural" -> StructuralDraftingIllustration(modifier = Modifier.fillMaxSize())
                            else -> MaterialSupplyIllustration(modifier = Modifier.fillMaxSize())
                        }
                    }

                    // Descriptions
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = service.desc,
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            lineHeight = 16.sp
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        // Highlights points
                        points.forEach { point ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = point,
                                    fontSize = 11.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Trigger actions
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.navigateTo(Screen.EnquiryForm(service.title))
                                },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Enquire Now", fontSize = 12.sp)
                            }
                            Button(
                                onClick = {
                                    viewModel.navigateTo(Screen.PaymentScreen)
                                },
                                modifier = Modifier.weight(1.2f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                            ) {
                                Text(text = "Book Order", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ServiceItemData(
    val id: Int,
    val title: String,
    val desc: String,
    val points: List<String>,
    val canvasElement: @Composable () -> Unit
)

// === 3. ENQUIRY / LEAD FORM SCREEN COMPOSABLE ===
@Composable
fun EnquiryFormScreen(viewModel: MainViewModel, autoFillCategory: String? = null) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Observe text inputs
    val name by viewModel.leadName.collectAsState()
    val phone by viewModel.leadPhone.collectAsState()
    val plotSize by viewModel.leadPlotSize.collectAsState()
    val location by viewModel.leadLocation.collectAsState()
    val requirement by viewModel.leadRequirement.collectAsState()
    val message by viewModel.leadMessage.collectAsState()

    // Pre-fill categories if redirected from service card
    LaunchedEffect(autoFillCategory) {
        if (autoFillCategory != null) {
            viewModel.leadRequirement.value = autoFillCategory
        }
    }

    var dropdownExpanded by remember { mutableStateOf(false) }
    var submitWithWhatsApp by remember { mutableStateOf(true) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    val categories = listOf(
        "House Planning & 2D/3D Design",
        "Construction (Turnkey Projects)",
        "Interior & Exterior Design",
        "Electrical, Plumbing & HVAC",
        "Structural Design & Approval",
        "Material Supply"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 18.dp, horizontal = 20.dp)
        ) {
            Text(
                text = "CUSTOM BLUEPRINTS & ESTIMATES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantBlue,
                letterSpacing = 1.6.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "Request a Quote",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = TextPrimary
            )
            Text(
                text = "Provide plot dimensions, project site, and requirement profiles to generate a precise custom proposal.",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        HorizontalDivider(color = SlateBorder, thickness = 1.dp)

        Spacer(modifier = Modifier.height(14.dp))

        if (isSubmittedSuccess) {
            // Elegant submission card success
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Submit Success emblem",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Enquiry Submitted!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Your request is registered successfully in our local database. Our regional engineer will coordinate with you inside 2 Hours.",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Button(
                        onClick = {
                            isSubmittedSuccess = false
                            viewModel.navigateTo(Screen.Home)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Back to Home Screen", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = { isSubmittedSuccess = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit Another Application", fontSize = 13.sp)
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Customer Leads Builder",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Input 1: Customer Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.leadName.value = it },
                        label = { Text("Your Full Name *") },
                        placeholder = { Text("Enter full name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("enquiry_input_name"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Input 2: Mobile Number
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { viewModel.leadPhone.value = it },
                        label = { Text("WhatsApp/Phone Number *") },
                        placeholder = { Text("10-digit mobile number") },
                        leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("enquiry_input_phone"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Input 3: Plot Size
                    OutlinedTextField(
                        value = plotSize,
                        onValueChange = { viewModel.leadPlotSize.value = it },
                        label = { Text("Plot Dimensions (Optional)") },
                        placeholder = { Text("e.g. 30 x 50 sqft") },
                        leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },//fallback icon
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("enquiry_input_plotsize"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Input 4: Plot Location
                    OutlinedTextField(
                        value = location,
                        onValueChange = { viewModel.leadLocation.value = it },
                        label = { Text("Project Location (Optional)") },
                        placeholder = { Text("e.g. Barhalganj, Gorakhpur") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("enquiry_input_location"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Input 5: Requirement Category Spinner dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = requirement,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Construction Service *") },
                            placeholder = { Text("Tap to select") },
                            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { dropdownExpanded = true }) {
                                    Icon(
                                        imageVector = if (dropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Expand Category list dropdown"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("enquiry_category_dropdown_trigger")
                                .clickable { dropdownExpanded = true },
                            shape = RoundedCornerShape(8.dp)
                        )
                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat, fontSize = 13.sp) },
                                    onClick = {
                                        viewModel.leadRequirement.value = cat
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Input 6: Custom Description / Requirements
                    OutlinedTextField(
                        value = message,
                        onValueChange = { viewModel.leadMessage.value = it },
                        label = { Text("Project Details / Message") },
                        placeholder = { Text("Describe details like number of storeys, room layouts, or material grades...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("enquiry_input_message"),
                        maxLines = 4,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // WhatsApp confirmation option toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { submitWithWhatsApp = !submitWithWhatsApp }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = submitWithWhatsApp,
                            onCheckedChange = { submitWithWhatsApp = it }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Instant WhatsApp Notification",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Redirects details to official chat helpline on click.",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Submission validation trigger
                    Button(
                        onClick = {
                            if (name.isEmpty() || phone.isEmpty() || requirement.isEmpty()) {
                                Toast.makeText(context, "Full Name, Mobile Number, and Service are mandatory!", Toast.LENGTH_LONG).show()
                                return@Button
                            }
                            if (phone.length < 10) {
                                Toast.makeText(context, "Please enter a valid 10-digit mobile number!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // Commit save process
                            viewModel.submitEnquiry {
                                isSubmittedSuccess = true
                                if (submitWithWhatsApp) {
                                    val waMsg = buildString {
                                        append("Hello Relief India Construction!\n\n")
                                        append("*New Enquiry Form Details:*\n")
                                        append("• *Name:* $name\n")
                                        append("• *Phone:* $phone\n")
                                        append("• *Service:* $requirement\n")
                                        if (plotSize.isNotEmpty()) append("• *Plot Size:* $plotSize\n")
                                        if (location.isNotEmpty()) append("• *Location:* $location\n")
                                        if (message.isNotEmpty()) append("• *Message:* $message\n")
                                        append("\nPlease share the layouts & rates catalog.")
                                    }
                                    triggerWhatsAppIntent(context, "+918574367610", waMsg)
                                } else {
                                    Toast.makeText(context, "Lead saved locally & sent to the admin registry!", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("enquiry_submit_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submit Architectural Request", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

// === 4. PAYMENT SECTION COMPOSABLE ===
@Composable
fun PaymentScreen(viewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val configs by viewModel.cmsConfigMap.collectAsState()

    val floorPlanFeeStr = configs["pricing_floor_plan_fee"] ?: "4999"
    val floorPlanFee = floorPlanFeeStr.toDoubleOrNull() ?: 4999.0
    val floorPlanFeeIntStr = floorPlanFee.toInt().toString()

    var selectedMethod by remember { mutableStateOf("UPI") }
    var payAmountType by remember { mutableStateOf("FloorPlan") } // "FloorPlan" or "CustomAdvance"
    var customAmountVal by remember { mutableStateOf("") }
    
    // Inputs (User info to audit payments records)
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }

    // States for showing biometric checkout sheet
    var isCheckingOut by remember { mutableStateOf(false) }
    var payTxnResult by remember { mutableStateOf<Payment?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 18.dp, horizontal = 20.dp)
        ) {
            Text(
                text = "SECURE DIGITAL PAYMENTS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantBlue,
                letterSpacing = 1.6.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "Escrow Gateway",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = TextPrimary
            )
            Text(
                text = "Authorized digital booking fees or custom advance construction balance transfers.",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        HorizontalDivider(color = SlateBorder, thickness = 1.dp)

        Spacer(modifier = Modifier.height(14.dp))

        if (payTxnResult != null) {
            // High fidelity payment receipt invoice display
            PaymentReceiptLayout(receipt = payTxnResult!!, onBackHome = {
                payTxnResult = null
                viewModel.navigateTo(Screen.Home)
            })
        } else if (isCheckingOut) {
            // Bio-screen checkout sheet loader
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Authenticating secure Razorpay portal...",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Verifying UPI handles, secure encryptions, & server assets...",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LaunchedEffect(Unit) {
                        delay(2800) // simulated payment buffer
                        val amountFinal = if (payAmountType == "FloorPlan") floorPlanFee else {
                            customAmountVal.toDoubleOrNull() ?: 1000.0
                        }
                        
                        viewModel.executePayment(
                            type = if (payAmountType == "FloorPlan") "Book Floor Plan" else "Advance Payment",
                            amount = amountFinal,
                            name = if (clientName.isEmpty()) "Test Client" else clientName,
                            phone = if (clientPhone.isEmpty()) "9999999999" else clientPhone,
                            onSuccess = { paymentRecord ->
                                isCheckingOut = false
                                payTxnResult = paymentRecord
                            }
                        )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Select Payment Category",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // payment option 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.5.dp,
                                color = if (payAmountType == "FloorPlan") MaterialTheme.colorScheme.primary else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { payAmountType = "FloorPlan" }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = payAmountType == "FloorPlan",
                            onClick = { payAmountType = "FloorPlan" }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Book CAD Floor Plan Architecture",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Pre-fixed price: ₹$floorPlanFeeIntStr (Inc taxes)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // payment option 2
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.5.dp,
                                color = if (payAmountType == "CustomAdvance") MaterialTheme.colorScheme.primary else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { payAmountType = "CustomAdvance" }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = payAmountType == "CustomAdvance",
                            onClick = { payAmountType = "CustomAdvance" }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Advance Construction Booking / Deposit",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Escrow account fund for running projects",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Custom Amount entry container
                    AnimatedVisibility(visible = payAmountType == "CustomAdvance") {
                        OutlinedTextField(
                            value = customAmountVal,
                            onValueChange = { customAmountVal = it },
                            label = { Text("Transfer Amount (₹)") },
                            placeholder = { Text("Enter customized amount") },
                            leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },//fallback icon
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("payment_custom_amount"),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Customer Ledger Log Info",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Full Name *") },
                        placeholder = { Text("Enter payee's name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("payment_input_name"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        label = { Text("WhatsApp Contact *") },
                        placeholder = { Text("10-digit number") },
                        leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("payment_input_phone"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Select Payment Gateway Mode",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MethodButton("Google Pay / UPI", selectedMethod == "UPI", Modifier.weight(1f)) { selectedMethod = "UPI" }
                        MethodButton("Razorpay / Cards", selectedMethod == "Razorpay", Modifier.weight(1f)) { selectedMethod = "Razorpay" }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    val priceCalculated = if (payAmountType == "FloorPlan") floorPlanFee else {
                        customAmountVal.toDoubleOrNull() ?: 0.0
                    }

                    Button(
                        onClick = {
                            if (clientName.trim().isEmpty() || clientPhone.trim().isEmpty()) {
                                Toast.makeText(context, "Please enter your ledger Name and Mobile Number!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (clientPhone.trim().length < 10) {
                                Toast.makeText(context, "Invalid 10-digit Phone number!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (priceCalculated <= 0.0) {
                                Toast.makeText(context, "Price transacted must be positive and greater than 0!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            isCheckingOut = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("payment_pay_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Proceed to Secure Pay ₹${String.format("%,.0f", priceCalculated)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MethodButton(label: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .border(
                1.5.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                RoundedCornerShape(8.dp)
            )
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.DarkGray
        )
    }
}

@Composable
fun PaymentReceiptLayout(receipt: com.example.data.Payment, onBackHome: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Color(0xFF2E7D32))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Receipt header logo
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success tick icon",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "TRANSACTION SUCCESSFUL",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Relief India Construction Pvt. Ltd.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider()
            Spacer(modifier = Modifier.height(14.dp))

            // Transaction detail list
            InvoiceRow("Receipt ID:", receipt.txnId)
            InvoiceRow("Payee Client Name:", receipt.customerName)
            InvoiceRow("WhatsApp logged:", receipt.customerPhone)
            InvoiceRow("Purpose Type:", receipt.paymentType)
            InvoiceRow("Status Badge:", "PAID Successfully")

            Spacer(modifier = Modifier.height(14.dp))
            Divider()
            Spacer(modifier = Modifier.height(14.dp))

            // Transacted amount block
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Charged:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "₹${String.format("%,.0f", receipt.amount)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Action triggers
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        val shareTxt = buildString {
                            append("*Payment Confirmation Receipt*\n")
                            append("• *Txn ID:* ${receipt.txnId}\n")
                            append("• *Company:* Relief India Construction Pvt. Ltd.\n")
                            append("• *Amount:* ₹${receipt.amount}\n")
                            append("• *Category:* ${receipt.paymentType}\n")
                            append("• *Client:* ${receipt.customerName}\n")
                            append("• *Invoice status:* PAID Success\n\n")
                            append("Thank you for choosing Relief India. Complete construction and design solutions!")
                        }
                        triggerWhatsAppIntent(context, receipt.customerPhone, shareTxt)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share Info", fontSize = 11.sp)
                }

                Button(
                    onClick = onBackHome,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Return Home", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun InvoiceRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}


// === 5. CONTACT SECTION COMPOSABLE ===
@Composable
fun ContactScreen(viewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val configs by viewModel.cmsConfigMap.collectAsState()

    val phone = configs["contact_phone"] ?: "+91-8574367610"
    val email = configs["contact_email"] ?: "reliefindiacpl@gmail.com"
    val address = configs["contact_address"] ?: "Relief India Complex, Patna Chauraha, Mukhtipath Road, Barhalganj, Gorakhpur, Uttar Pradesh – 273402"
    val cleanPhone = phone.replace("-", "").replace(" ", "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 18.dp, horizontal = 20.dp)
        ) {
            Text(
                text = "DIRECT HELPLINE & OFFICE DIRECTORY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantBlue,
                letterSpacing = 1.6.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "Get in Touch",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = TextPrimary
            )
            Text(
                text = "Reach our headquarters, send emails, or chat with our online engineers.",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        HorizontalDivider(color = SlateBorder, thickness = 1.dp)

        Spacer(modifier = Modifier.height(14.dp))

        // Coordinates Cards detail sheets
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Headquarters detail card
                Text(
                    text = "Regional Headquarters",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                
                Text(
                    text = address,
                    fontSize = 12.sp,
                    color = TextPrimary,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                Divider()
                Spacer(modifier = Modifier.height(14.dp))

                // Direct Clickable Action row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButtonBox(
                        title = "Call Helpline",
                        subtitle = phone,
                        icon = Icons.Default.Call,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    ) {
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
                        context.startActivity(callIntent)
                    }

                    IconButtonBox(
                        title = "Email Office",
                        subtitle = email,
                        icon = Icons.Default.Email,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    ) {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$email")
                            putExtra(Intent.EXTRA_SUBJECT, "Customer Enquiry: Relief India Construction")
                        }
                        try {
                            context.startActivity(Intent.createChooser(emailIntent, "Send Email..."))
                        } catch (e: Exception) {
                            Toast.makeText(context, "No email client found!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interacting Office Gps Map Visual layout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Office Geo Locator",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Canvas drawn map vector
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    OfficeGpsMapIllustration(modifier = Modifier.fillMaxSize())
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val mapsUri = Uri.parse("geo:26.2773,83.4984?q=Relief+India+Complex+Barhalganj+Gorakhpur")
                        val mapIntent = Intent(Intent.ACTION_VIEW, mapsUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        try {
                            context.startActivity(mapIntent)
                        } catch (e: Exception) {
                            // Trigger fallback browser geo map
                            val browserMapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/c6j1G1tQJm"))
                            context.startActivity(browserMapsIntent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Trigger Directions in Google Maps", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun IconButtonBox(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Global Help utility to launch WhatsApp chat with pre-baked context messages
fun triggerWhatsAppIntent(context: Context, phone: String, message: String) {
    // Standard phone stripping to match international payloads
    val cleanedPhone = phone.replace("+", "").replace("-", "").trim()
    val waUrl = "https://api.whatsapp.com/send?phone=$cleanedPhone&text=${Uri.encode(message)}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(waUrl))
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp integration failed or isn't installed. Initiating direct text draft instead!", Toast.LENGTH_LONG).show()
        val textBackup = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone")).apply {
            putExtra("sms_body", message)
        }
        context.startActivity(textBackup)
    }
}
