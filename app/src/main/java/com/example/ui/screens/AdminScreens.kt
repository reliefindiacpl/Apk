package com.example.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import com.example.data.*
import com.example.ui.theme.*
import com.example.ui.MainViewModel
import com.example.ui.Screen
import java.text.SimpleDateFormat
import java.util.*

// Root secure admin container routing screen
@Composable
fun AdminPortalScreen(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (currentScreen) {
            is Screen.AdminLogin -> AdminLoginLayout(viewModel)
            is Screen.AdminDashboard -> AdminDashboardLayout(viewModel)
            else -> AdminDashboardLayout(viewModel)
        }
    }
}

// === 1. SECURE LOGIN VIEW ==
@Composable
fun AdminLoginLayout(viewModel: MainViewModel) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Visual Keylock Logo
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "RIC Admin Portal",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Internal engineering and lead dispatch console.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Testing sandbox hints alert card (Mandatory visual helper)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
            border = BorderStroke(1.dp, Color(0xFFFBC02D))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Diagnostic warning",
                        tint = Color(0xFFF57F17),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Testing Environment Login Credentials:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Username: admin\n• Password: reliefindia123",
                    fontSize = 11.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Inputs forms
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                loginError = false
            },
            label = { Text("Admin Username") },
            placeholder = { Text("admin") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_login_username"),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                loginError = false
            },
            label = { Text("Security Access Password") },
            placeholder = { Text("reliefindia123") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_login_password"),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        AnimatedVisibility(visible = loginError) {
            Text(
                text = "Invalid credentials. Please refer to yellow notes!",
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val isSuccess = viewModel.adminLogin(username, password)
                if (isSuccess) {
                    Toast.makeText(context, "Access Granted. Initializing Admin Console!", Toast.LENGTH_SHORT).show()
                } else {
                    loginError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("admin_login_submit_button"),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Verify Credentials & Enter", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { viewModel.navigateTo(Screen.Home) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Return to Customer Site Screen", fontSize = 12.sp)
        }
    }
}

// === 2. DASHBOARD & LEADS LISTING CONTROLLER ===
@Composable
fun AdminDashboardLayout(viewModel: MainViewModel) {
    val context = LocalContext.current
    val enquiries by viewModel.enquiries.collectAsState()
    val payments by viewModel.payments.collectAsState()

    var activeTab by remember { mutableStateOf("Leads") } // "Leads" or "Payments"
    var filterStatus by remember { mutableStateOf("All") } // "All", "New", "Contacted", "Closed"

    // Process statistics
    val totalLeads = enquiries.size
    val newLeads = enquiries.count { it.status.equals("New", ignoreCase = true) }
    val contactedLeads = enquiries.count { it.status.equals("Contacted", ignoreCase = true) }
    val closedLeads = enquiries.count { it.status.equals("Closed", ignoreCase = true) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "RIC Admin Dashboard",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Relief India Construction Registry Panel",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    IconButton(
                        onClick = { viewModel.adminLogout() },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Log out from Admin console",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    ) { parentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(parentPadding)
                .background(Color(0xFFF1F3F6))
        ) {
            // Summary KPI cards Grid (Lazy row simulation)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 16.dp, top = 14.dp, end = 16.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatusCardSummary("Total Leads", totalLeads.toString(), MaterialTheme.colorScheme.primary)
                StatusCardSummary("New Enquiries", newLeads.toString(), MaterialTheme.colorScheme.secondary)
                StatusCardSummary("Contacted", contactedLeads.toString(), Color(0xFFF57C00))
                StatusCardSummary("Closed Contracts", closedLeads.toString(), Color(0xFF388E3C))
            }

            // Tab bar switcher (Leads / Payments / CMS)
            TabRow(
                selectedTabIndex = when (activeTab) {
                    "Leads" -> 0
                    "Payments" -> 1
                    else -> 2
                },
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Tab(
                    selected = activeTab == "Leads",
                    onClick = { activeTab = "Leads" },
                    text = { Text("Leads", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeTab == "Payments",
                    onClick = { activeTab = "Payments" },
                    text = { Text("Bookings", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeTab == "CMS",
                    onClick = { activeTab = "CMS" },
                    text = { Text("CMS Control", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }

            // Tab Content
            when (activeTab) {
                "Leads" -> {
                    // Filters row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterPill("All Leads", filterStatus == "All") { filterStatus = "All" }
                        FilterPill("New", filterStatus == "New") { filterStatus = "New" }
                        FilterPill("Contacted", filterStatus == "Contacted") { filterStatus = "Contacted" }
                        FilterPill("Closed", filterStatus == "Closed") { filterStatus = "Closed" }
                    }

                    // Feed list
                    val filteredList = when (filterStatus) {
                        "All" -> enquiries
                        else -> enquiries.filter { it.status.equals(filterStatus, ignoreCase = true) }
                    }

                    if (filteredList.isEmpty()) {
                        EmptyLogPlaceholder(
                            title = "No Prospects Registered",
                            msg = "Customers requesting quotes or floor plans will reflect reactively here in real-time."
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredList, key = { it.id }) { enquiry ->
                                LeadConsoleItemCard(
                                    enquiry = enquiry,
                                    onStatusChange = { stat -> viewModel.updateLeadStatus(enquiry, stat) },
                                    onNotesChange = { notes -> viewModel.updateLeadNotes(enquiry, notes) },
                                    onFollowUpSet = { date -> viewModel.updateLeadFollowUp(enquiry, date) },
                                    onDelete = { viewModel.deleteLead(enquiry.id) }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(20.dp)) }
                        }
                    }
                }
                "Payments" -> {
                    if (payments.isEmpty()) {
                        EmptyLogPlaceholder(
                            title = "No Digital Sales Registered",
                            msg = "Payees checking out blueprint plan packages or advance construction escrow funds will reflect reactively here in real-time."
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(payments, key = { it.id }) { payee ->
                                ClientPaymentAuditCard(payee)
                            }
                            item { Spacer(modifier = Modifier.height(20.dp)) }
                        }
                    }
                }
                "CMS" -> {
                    CmsPanelManager(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun StatusCardSummary(title: String, score: String, color: Color) {
    Card(
        modifier = Modifier
            .width(116.dp)
            .height(68.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 10.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = score, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun FilterPill(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White)
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.6f),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.DarkGray
        )
    }
}

@Composable
fun EmptyLogPlaceholder(title: String, msg: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(54.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateDark)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = msg,
            fontSize = 11.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 15.sp
        )
    }
}

// Expandable list items for lead handling console
@Composable
fun LeadConsoleItemCard(
    enquiry: Enquiry,
    onStatusChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onFollowUpSet: (Long?) -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var notesLocal by remember { mutableStateOf(enquiry.adminNotes) }
    var dropdownChoiceOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val simpleDateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
    val formattedTimestamp = simpleDateFormat.format(Date(enquiry.timestamp))

    val statusBadgeColor = when (enquiry.status) {
        "New" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
        "Contacted" -> Color(0xFFFFF3E0)
        "Closed" -> Color(0xFFE8F5E9)
        else -> Color.LightGray
    }

    val statusTxtColor = when (enquiry.status) {
        "New" -> MaterialTheme.colorScheme.secondary
        "Contacted" -> Color(0xFFE65100)
        "Closed" -> Color(0xFF2E7D32)
        else -> Color.DarkGray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lead_item_card_${enquiry.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Name + category badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = enquiry.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Captured: $formattedTimestamp", fontSize = 10.sp, color = Color.Gray)
                }
                
                // Status indicators chip dropdown wrapper
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(statusBadgeColor)
                            .clickable { dropdownChoiceOpen = true }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = enquiry.status.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = statusTxtColor
                        )
                    }
                    
                    DropdownMenu(
                        expanded = dropdownChoiceOpen,
                        onDismissRequest = { dropdownChoiceOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("NEW", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary) },
                            onClick = {
                                onStatusChange("New")
                                dropdownChoiceOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("CONTACTED", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100)) },
                            onClick = {
                                onStatusChange("Contacted")
                                dropdownChoiceOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("CLOSED", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32)) },
                            onClick = {
                                onStatusChange("Closed")
                                dropdownChoiceOpen = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(10.dp))

            // Main highlights (Service requested & Phone)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Requested Service:", fontSize = 10.sp, color = Color.Gray)
                    Text(text = enquiry.requirement, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "WhatsApp Mobile:", fontSize = 10.sp, color = Color.Gray)
                    Text(text = enquiry.phone, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            // Expandable segment trigger
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expanded) "Collapse Admin Panel" else "Expand Admin Panel & Details",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Expanded detail fields
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Divider(color = Color.LightGray.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Plot Dimensions: ${enquiry.plotSize}", fontSize = 11.sp, color = TextPrimary)
                    Text(text = "Plot Location: ${enquiry.location}", fontSize = 11.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Prospect Description Message:", fontSize = 10.sp, color = Color.Gray)
                    Text(
                        text = enquiry.message,
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        lineHeight = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F7FA), RoundedCornerShape(6.dp))
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Reminders & follow-up datetime indicators
                    val followDateFmt = enquiry.followUpDate?.let {
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it))
                    } ?: "No Follow-up Scheduled"
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEDE7F6))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color(0xFF5E35B1), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Followup: $followDateFmt", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5E35B1))
                        }
                        
                        // Small trigger button to pick follow-up date
                        TextButton(
                            onClick = {
                                showAndroidDatePicker(context) { datePicked ->
                                    onFollowUpSet(datePicked.timeInMillis)
                                }
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Schedule", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5E35B1))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Admin memo notes input
                    OutlinedTextField(
                        value = notesLocal,
                        onValueChange = {
                            notesLocal = it
                            onNotesChange(it)
                        },
                        label = { Text("Internal Follow-up Notes (Saves Auto)") },
                        placeholder = { Text("e.g., Prefers turn-key block pricing. Promised site visit tomorrow...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("lead_notes_input_${enquiry.id}"),
                        textStyle = TextStyle(fontSize = 11.sp),
                        shape = RoundedCornerShape(6.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Interactive call helper buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val telIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${enquiry.phone}"))
                                context.startActivity(telIntent)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Dial", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                val followupMsg = "Hello ${enquiry.name}, I am coordinating from the Relief India Construction team regarding your structural enquiry on *${enquiry.requirement}*. We reviewed your details, shall we schedule a call?"
                                triggerWhatsAppIntent(context, enquiry.phone, followupMsg)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            modifier = Modifier.weight(1.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))//fallback
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFEBEE))
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete customer lead", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

// Dialog picker for schedules dates
private fun showAndroidDatePicker(context: Context, onDateSet: (Calendar) -> Unit) {
    val cal = Calendar.getInstance()
    val dialog = DatePickerDialog(
        context,
        { _: DatePicker, y: Int, m: Int, d: Int ->
            val setCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, y)
                set(Calendar.MONTH, m)
                set(Calendar.DAY_OF_MONTH, d)
            }
            onDateSet(setCal)
        },
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    )
    dialog.show()
}

// Detailed row logging payment logs
@Composable
fun ClientPaymentAuditCard(payment: Payment) {
    val dateString = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(payment.timestamp))
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = payment.customerName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Time: $dateString", fontSize = 10.sp, color = Color.Gray)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = payment.paymentStatus.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(10.dp))

            InvoiceRow("Transacted Txn ID:", payment.txnId)
            InvoiceRow("Filing Purpose:", payment.paymentType)
            InvoiceRow("WhatsApp Logged:", payment.customerPhone)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val shareTxt = buildString {
                            append("*Official Audit Receipt*\n")
                            append("• *Txn:* ${payment.txnId}\n")
                            append("• *Client:* ${payment.customerName}\n")
                            append("• *Charged:* ₹${payment.amount}\n")
                            append("• *Type:* ${payment.paymentType}\n")
                            append("• *Date:* $dateString\n")
                            append("Verified by Relief India Construction Panel Ledger.")
                        }
                        triggerWhatsAppIntent(context, payment.customerPhone, shareTxt)
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFECEFF1))
                        .size(34.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share payment receipt", tint = Color.DarkGray, modifier = Modifier.size(16.dp))
                }
                
                Text(
                    text = "₹${String.format("%,.0f", payment.amount)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// === 3. CMS CONTENT MANAGEMENT CONTROL PANEL ===
@Composable
fun CmsPanelManager(viewModel: MainViewModel) {
    val configs by viewModel.cmsConfigMap.collectAsState()
    val services by viewModel.cmsServices.collectAsState()
    val slides by viewModel.cmsSlides.collectAsState()
    val context = LocalContext.current

    var cmsSubTab by remember { mutableStateOf("Configs") } // "Configs", "Services", "Slides"

    Column(modifier = Modifier.fillMaxSize()) {
        // Sub-tabs switcher row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SubTabPill("Configs", cmsSubTab == "Configs") { cmsSubTab = "Configs" }
            SubTabPill("Services (${services.size})", cmsSubTab == "Services") { cmsSubTab = "Services" }
            SubTabPill("Banners (${slides.size})", cmsSubTab == "Slides") { cmsSubTab = "Slides" }
        }

        Divider(color = Color.LightGray.copy(alpha = 0.5f))

        Box(modifier = Modifier.weight(1f)) {
            when (cmsSubTab) {
                "Configs" -> ConfigsManagerTab(viewModel, configs)
                "Services" -> ServicesManagerTab(viewModel, services)
                "Slides" -> SlidesManagerTab(viewModel, slides)
            }
        }
    }
}

@Composable
fun ConfigsManagerTab(viewModel: MainViewModel, configs: Map<String, String>) {
    val context = LocalContext.current
    
    // Config states
    var phone by remember(configs) { mutableStateOf(configs["contact_phone"] ?: "") }
    var email by remember(configs) { mutableStateOf(configs["contact_email"] ?: "") }
    var address by remember(configs) { mutableStateOf(configs["contact_address"] ?: "") }
    var whatsapp by remember(configs) { mutableStateOf(configs["contact_whatsapp"] ?: "") }
    var floorPlanFee by remember(configs) { mutableStateOf(configs["pricing_floor_plan_fee"] ?: "") }
    var promoText by remember(configs) { mutableStateOf(configs["promo_alert_text"] ?: "") }
    
    var showAlert by remember(configs) { mutableStateOf(configs["show_alert_banner"] ?: "true") }
    var showBanners by remember(configs) { mutableStateOf(configs["show_banners_slider"] ?: "true") }
    var showQuickActions by remember(configs) { mutableStateOf(configs["show_quick_actions"] ?: "true") }
    var showWhyUs by remember(configs) { mutableStateOf(configs["show_why_choose_us"] ?: "true") }
    var showTestimonials by remember(configs) { mutableStateOf(configs["show_testimonials"] ?: "true") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Contact & Support Defaults", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
                
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Phone Helpline") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Contact Email Support") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text("WhatsApp Helpline Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("HQ Office Address") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = floorPlanFee,
                    onValueChange = { floorPlanFee = it },
                    label = { Text("Standard CAD Floor Plan Fee (INR)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Layout Controls & Dynamic Enablers", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show Special Promo Alert Box", fontSize = 12.sp, color = Color.DarkGray)
                    Switch(checked = showAlert == "true", onCheckedChange = { showAlert = if (it) "true" else "false" })
                }

                if (showAlert == "true") {
                    OutlinedTextField(
                        value = promoText,
                        onValueChange = { promoText = it },
                        label = { Text("Special Promo Banner Message") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show Auto-Scrolling Hero Slider", fontSize = 12.sp, color = Color.DarkGray)
                    Switch(checked = showBanners == "true", onCheckedChange = { showBanners = if (it) "true" else "false" })
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show Quick Action Short-links", fontSize = 12.sp, color = Color.DarkGray)
                    Switch(checked = showQuickActions == "true", onCheckedChange = { showQuickActions = if (it) "true" else "false" })
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show 'Why Choose Us' Grid", fontSize = 12.sp, color = Color.DarkGray)
                    Switch(checked = showWhyUs == "true", onCheckedChange = { showWhyUs = if (it) "true" else "false" })
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show Client Testimonials", fontSize = 12.sp, color = Color.DarkGray)
                    Switch(checked = showTestimonials == "true", onCheckedChange = { showTestimonials = if (it) "true" else "false" })
                }
            }
        }

        Button(
            onClick = {
                viewModel.updateCmsConfigValue("contact_phone", phone)
                viewModel.updateCmsConfigValue("contact_email", email)
                viewModel.updateCmsConfigValue("contact_address", address)
                viewModel.updateCmsConfigValue("contact_whatsapp", whatsapp)
                viewModel.updateCmsConfigValue("pricing_floor_plan_fee", floorPlanFee)
                viewModel.updateCmsConfigValue("promo_alert_text", promoText)
                
                viewModel.updateCmsConfigValue("show_alert_banner", showAlert)
                viewModel.updateCmsConfigValue("show_banners_slider", showBanners)
                viewModel.updateCmsConfigValue("show_quick_actions", showQuickActions)
                viewModel.updateCmsConfigValue("show_why_choose_us", showWhyUs)
                viewModel.updateCmsConfigValue("show_testimonials", showTestimonials)
                
                Toast.makeText(context, "Configurations saved! Live app updated.", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Apply & Update live layouts", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ServicesManagerTab(viewModel: MainViewModel, services: List<CmsService>) {
    val context = LocalContext.current
    var isAddingNew by remember { mutableStateOf(false) }
    var editingService by remember { mutableStateOf<CmsService?>(null) }

    // Service Input fields
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var pointsCsv by remember { mutableStateOf("") }
    var categoryType by remember { mutableStateOf("design") } // design, construction, interior, structural, material

    // Reset inputs helper
    fun resetInputs() {
        title = ""
        desc = ""
        pointsCsv = ""
        categoryType = "design"
        isAddingNew = false
        editingService = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isAddingNew || editingService != null) {
            // Edit / Add Card Container form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (editingService != null) "Edit Service Detail" else "Add Custom Solution Key",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Service Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Detailed Service Description") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )

                    OutlinedTextField(
                        value = pointsCsv,
                        onValueChange = { pointsCsv = it },
                        label = { Text("Key Highlights (comma-separated)") },
                        placeholder = { Text("High Grade Cement, Fast execution, Leak-proof") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Illustrations matcher dropdown simulation
                    Text("Specialized Canvas Category Vector Model", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("design" to "Architecture", "construction" to "Civil Work", "interior" to "Modular Room", "structural" to "Structural Beams", "material" to "Deliveries").forEach { item ->
                            val isSelected = categoryType == item.first
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.secondary else Color(0xFFECEFF1))
                                    .clickable { categoryType = item.first }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = item.second,
                                    fontSize = 11.sp,
                                    color = if (isSelected) Color.White else Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { resetInputs() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                if (title.trim().isEmpty() || desc.trim().isEmpty() || pointsCsv.trim().isEmpty()) {
                                    Toast.makeText(context, "All parameters must be set!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (editingService != null) {
                                    val updated = editingService!!.copy(
                                        title = title,
                                        desc = desc,
                                        pointsCsv = pointsCsv,
                                        categoryType = categoryType
                                    )
                                    viewModel.updateCmsService(updated)
                                    Toast.makeText(context, "Turnkey service updated!", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.addCmsService(title, desc, pointsCsv, categoryType)
                                    Toast.makeText(context, "New turnkey service added live!", Toast.LENGTH_SHORT).show()
                                }
                                resetInputs()
                            },
                            modifier = Modifier.weight(1.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Commit Service")
                        }
                    }
                }
            }
        } else {
            // "Add New Service" top banner trigger
            Button(
                onClick = {
                    title = ""
                    desc = ""
                    pointsCsv = ""
                    categoryType = "design"
                    isAddingNew = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Turnkey Service Offer", fontWeight = FontWeight.Bold)
                }
            }
        }

        // List of existing services
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(services, key = { it.id }) { service ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(service.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Vector Model: ${service.categoryType.uppercase()}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = {
                                        editingService = service
                                        title = service.title
                                        desc = service.desc
                                        pointsCsv = service.pointsCsv
                                        categoryType = service.categoryType
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.deleteCmsService(service.id)
                                        Toast.makeText(context, "Turnkey Service deleted!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(service.desc, fontSize = 11.sp, color = Color.DarkGray, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Points: ${service.pointsCsv}", fontSize = 10.sp, color = Color.Gray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    }
                }
            }
        }
    }
}

@Composable
fun SlidesManagerTab(viewModel: MainViewModel, slides: List<CmsSlide>) {
    val context = LocalContext.current
    var isAddingNew by remember { mutableStateOf(false) }
    var editingSlide by remember { mutableStateOf<CmsSlide?>(null) }

    // Slide Input fields
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var btnText by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("Enquiry") } // Enquiry, Services, Payment
    var categoryType by remember { mutableStateOf("design") } // design, construction, interior, structural, material

    // Reset inputs helper
    fun resetInputs() {
        title = ""
        desc = ""
        btnText = ""
        destination = "Enquiry"
        categoryType = "design"
        isAddingNew = false
        editingSlide = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isAddingNew || editingSlide != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (editingSlide != null) "Edit Banner Slide" else "Add Advertisement Banner",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Banner Title Header") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Banner Promotional Text") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    OutlinedTextField(
                        value = btnText,
                        onValueChange = { btnText = it },
                        label = { Text("Button Text") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Destination screen dropdown simulation
                    Text("Slide Navigation Destination Screen", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Enquiry" to "Enquiry Panel", "Services" to "Services Tab", "Payment" to "Payment Gateway").forEach { dest ->
                            val isSelected = destination == dest.first
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFECEFF1))
                                    .clickable { destination = dest.first }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(dest.second, fontSize = 10.sp, color = if (isSelected) Color.White else Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Illustrations matcher dropdown simulation
                    Text("Slide Canvas Category Vector Model", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("design" to "Architecture", "construction" to "Civil Work", "interior" to "Modular kitchen", "structural" to "Structural beams", "material" to "Deliveries").forEach { item ->
                            val isSelected = categoryType == item.first
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.secondary else Color(0xFFECEFF1))
                                    .clickable { categoryType = item.first }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = item.second,
                                    fontSize = 11.sp,
                                    color = if (isSelected) Color.White else Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { resetInputs() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                if (title.trim().isEmpty() || desc.trim().isEmpty() || btnText.trim().isEmpty()) {
                                    Toast.makeText(context, "All parameters must be set!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (editingSlide != null) {
                                    val updated = editingSlide!!.copy(
                                        title = title,
                                        desc = desc,
                                        btnText = btnText,
                                        destination = destination,
                                        categoryType = categoryType
                                    )
                                    viewModel.updateCmsSlide(updated)
                                    Toast.makeText(context, "In-app promotion banner updated!", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.addCmsSlide(title, desc, btnText, destination, categoryType)
                                    Toast.makeText(context, "New promotion banner added live!", Toast.LENGTH_SHORT).show()
                                }
                                resetInputs()
                            },
                            modifier = Modifier.weight(1.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Commit Banner")
                        }
                    }
                }
            }
        } else {
            Button(
                onClick = {
                    title = ""
                    desc = ""
                    btnText = ""
                    destination = "Enquiry"
                    categoryType = "design"
                    isAddingNew = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Promotion Slide Banner", fontWeight = FontWeight.Bold)
                }
            }
        }

        // List of existing slides
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(slides, key = { it.id }) { slide ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(slide.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "Model: ${slide.categoryType.uppercase()}",
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Target: ${slide.destination.uppercase()}",
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = {
                                        editingSlide = slide
                                        title = slide.title
                                        desc = slide.desc
                                        btnText = slide.btnText
                                        destination = slide.destination
                                        categoryType = slide.categoryType
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.deleteCmsSlide(slide.id)
                                        Toast.makeText(context, "Promotion slide deleted!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(slide.desc, fontSize = 11.sp, color = Color.DarkGray)
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Trigger: ${slide.btnText}", fontSize = 10.sp, color = Color.Gray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    }
                }
            }
        }
    }
}

@Composable
fun SubTabPill(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFECEFF1))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}
