package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke

// Reusable custom vectors designed using Compose Canvas
@Composable
fun CompanyLogoDrawing(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Draw "RI" purple-pink background circle
        val rCenterY = h * 0.45f
        val rCenterX = w * 0.22f
        val rRadius = w * 0.16f
        
        // Circular pink gradient ring
        drawCircle(
            color = Color(0xFFD81B60),
            radius = rRadius,
            center = Offset(rCenterX, rCenterY),
            style = Stroke(width = w * 0.03f)
        )
        // Accent crescent
        drawArc(
            color = Color(0xFF5E35B1),
            startAngle = 135f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(rCenterX - rRadius + 5f, rCenterY - rRadius + 5f),
            size = Size(rRadius * 2 - 10f, rRadius * 2 - 10f),
            style = Stroke(width = w * 0.035f)
        )

        // Draw letter shapes inside
        // Bottom bar
        drawRoundRect(
            color = Color(0xFFD81B60),
            topLeft = Offset(w * 0.08f, h * 0.45f),
            size = Size(w * 0.35f, h * 0.06f),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // 2. Horizontal separator bridge in Red
        drawRoundRect(
            color = Color(0xFFD81B60),
            topLeft = Offset(w * 0.35f, h * 0.45f),
            size = Size(w * 0.55f, h * 0.06f),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // 3. Right House Roofs in Deep Blue
        val houseColor = Color(0xFF0D47A1)
        val roofPath = Path().apply {
            moveTo(w * 0.42f, h * 0.38f)
            lineTo(w * 0.60f, h * 0.20f)
            lineTo(w * 0.72f, h * 0.38f)
            close()
        }
        drawPath(path = roofPath, color = houseColor)

        // Nested bigger roof
        val roofPath2 = Path().apply {
            moveTo(w * 0.51f, h * 0.44f)
            lineTo(w * 0.74f, h * 0.16f)
            lineTo(w * 0.92f, h * 0.43f)
            close()
        }
        drawPath(path = roofPath2, color = houseColor)

        // Skyscraper pillar block behind
        drawRect(
            color = Color(0xFF1E88E5),
            topLeft = Offset(w * 0.70f, h * 0.05f),
            size = Size(w * 0.12f, h * 0.25f)
        )
        // High-rise horizontal window lines
        for (i in 1..4) {
            val yOffset = h * 0.05f + (i * h * 0.05f)
            drawLine(
                color = Color.White,
                start = Offset(w * 0.72f, yOffset),
                end = Offset(w * 0.80f, yOffset),
                strokeWidth = 3f
            )
        }

        // Window for the main layout roof
        drawRect(
            color = Color.White,
            topLeft = Offset(w * 0.71f, h * 0.33f),
            size = Size(w * 0.06f, w * 0.06f)
        )
        // Crosshair of the window
        drawLine(
            color = houseColor,
            start = Offset(w * 0.74f, h * 0.33f),
            end = Offset(w * 0.74f, h * 0.33f + w * 0.06f),
            strokeWidth = 3f
        )
        drawLine(
            color = houseColor,
            start = Offset(w * 0.71f, h * 0.33f + w * 0.03f),
            end = Offset(w * 0.77f, h * 0.33f + w * 0.03f),
            strokeWidth = 3f
        )
    }
}

@Composable
fun BlueprintDraftingIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Blueprint blue background color
        drawRect(color = Color(0xFF0F2C59))

        // Grid lines to look like drafting pad
        val colCount = 12
        val colWidth = w / colCount
        for (i in 0..colCount) {
            drawLine(
                color = Color(0xFFFFFFFF).copy(alpha = 0.15f),
                start = Offset(i * colWidth, 0f),
                end = Offset(i * colWidth, h),
                strokeWidth = 1f
            )
        }
        val rowCount = 8
        val rowHeight = h / rowCount
        for (i in 0..rowCount) {
            drawLine(
                color = Color(0xFFFFFFFF).copy(alpha = 0.15f),
                start = Offset(0f, i * rowHeight),
                end = Offset(w, i * rowHeight),
                strokeWidth = 1f
            )
        }

        // Draw 2D House walls (white layout lines)
        val insetX = w * 0.15f
        val insetY = h * 0.18f
        val houseW = w * 0.7f
        val houseH = h * 0.65f

        // Outer walls
        val stroke = Stroke(width = 4f)
        drawRect(
            color = Color(0xFF4FC3F7),
            topLeft = Offset(insetX, insetY),
            size = Size(houseW, houseH),
            style = stroke
        )

        // Internal rooms dividers
        // Living Room divider
        drawLine(
            color = Color(0xFF81D4FA),
            start = Offset(insetX + houseW * 0.45f, insetY),
            end = Offset(insetX + houseW * 0.45f, insetY + houseH),
            strokeWidth = 4f
        )
        // Kitchen divider
        drawLine(
            color = Color(0xFF81D4FA),
            start = Offset(insetX + houseW * 0.45f, insetY + houseH * 0.5f),
            end = Offset(insetX + houseW, insetY + houseH * 0.5f),
            strokeWidth = 4f
        )

        // Bathroom divider
        drawLine(
            color = Color(0xFF81D4FA),
            start = Offset(insetX, insetY + houseH * 0.62f),
            end = Offset(insetX + houseW * 0.45f, insetY + houseH * 0.62f),
            strokeWidth = 4f
        )

        // Door arcs swings (classic blueprints!)
        val doorRad = houseH * 0.15f
        drawArc(
            color = Color(0xFFFFB74D),
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(insetX + houseW * 0.45f - doorRad, insetY + houseH * 0.2f),
            size = Size(doorRad * 2, doorRad * 2),
            style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f)))
        )

        // Dimension text labels markers
        // Dimensions lines (arrows on top & bottom)
        drawLine(
            color = Color(0xFF81D4FA),
            start = Offset(insetX, insetY - 15f),
            end = Offset(insetX + houseW, insetY - 15f),
            strokeWidth = 2f
        )
        drawLine(
            color = Color(0xFF81D4FA),
            start = Offset(insetX, insetY - 25f),
            end = Offset(insetX, insetY - 5f),
            strokeWidth = 2f
        )
        drawLine(
            color = Color(0xFF81D4FA),
            start = Offset(insetX + houseW, insetY - 25f),
            end = Offset(insetX + houseW, insetY - 5f),
            strokeWidth = 2f
        )

        // Draw elevation roof triangles in blueprint fashion (dotted/dash indicators)
        val roofPath = Path().apply {
            moveTo(insetX, insetY)
            lineTo(insetX + houseW * 0.5f, insetY - h * 0.12f)
            lineTo(insetX + houseW, insetY)
        }
        drawPath(
            path = roofPath,
            color = Color(0xFFB3E5FC),
            style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f)))
        )
    }
}

@Composable
fun ConstructionSiteIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Blue sky gradient style simulation
        drawRect(color = Color(0xFFECEFF1))

        // Ground/soil representation
        drawRect(
            color = Color(0xFFD7CCC8),
            topLeft = Offset(0f, h * 0.85f),
            size = Size(w, h * 0.15f)
        )

        // Concrete pillars & foundation base line
        drawRect(
            color = Color(0xFFB0BEC5),
            topLeft = Offset(w * 0.1f, h * 0.8f),
            size = Size(w * 0.8f, h * 0.06f)
        )

        // vertical steel reinforcement pillars
        val pillarW = w * 0.08f
        val pillarH = h * 0.5f

        val pillarsX = listOf(w * 0.2f, w * 0.46f, w * 0.72f)
        for (px in pillarsX) {
            // Main Concrete column
            drawRect(
                color = Color(0xFFCFD8DC),
                topLeft = Offset(px, h * 0.3f),
                size = Size(pillarW, pillarH)
            )

            // Scaffold reinforcement grill (criss-cross steel mesh)
            val step = h * 0.08f
            for (offsetY in 0..5) {
                val curY = h * 0.3f + (offsetY * step)
                if (curY + step <= h * 0.8f) {
                    // X crossbars
                    drawLine(
                        color = Color(0xFFDC2626).copy(alpha = 0.5f),
                        start = Offset(px, curY),
                        end = Offset(px + pillarW, curY + step),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color(0xFFDC2626).copy(alpha = 0.5f),
                        start = Offset(px + pillarW, curY),
                        end = Offset(px, curY + step),
                        strokeWidth = 3f
                    )
                }
            }

            // Pillar foundation steel rebars sticking out the top
            drawLine(
                color = Color(0xFF78909C),
                start = Offset(px + pillarW * 0.3f, h * 0.3f),
                end = Offset(px + pillarW * 0.3f, h * 0.22f),
                strokeWidth = 4f
            )
            drawLine(
                color = Color(0xFF78909C),
                start = Offset(px + pillarW * 0.7f, h * 0.3f),
                end = Offset(px + pillarW * 0.7f, h * 0.22f),
                strokeWidth = 4f
            )
        }

        // Horizontal structural support beams
        drawRect(
            color = Color(0xFF90A4AE),
            topLeft = Offset(w * 0.15f, h * 0.42f),
            size = Size(w * 0.7f, h * 0.04f)
        )

        // Brick wall partially completed
        val brickW = w * 0.06f
        val brickH = h * 0.03f
        val brickSpacing = 3f

        for (row in 0..4) {
            val brickY = h * 0.65f + (row * brickH)
            val offsetShift = if (row % 2 == 0) brickW / 2 else 0f
            for (col in 0..5) {
                val brickX = w * 0.28f + (col * brickW) + offsetShift
                if (brickX + brickW <= w * 0.7f) {
                    drawRoundRect(
                        color = Color(0xFFD84315),
                        topLeft = Offset(brickX, brickY),
                        size = Size(brickW - brickSpacing, brickH - brickSpacing),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
            }
        }
    }
}

@Composable
fun InteriorExteriorDesignIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Elegant living room wall canvas base
        drawRect(color = Color(0xFFECEFF1))

        // Large feature modern glass window pane to external view
        drawRoundRect(
            color = Color(0xFFCFD8DC),
            topLeft = Offset(w * 0.45f, h * 0.15f),
            size = Size(w * 0.45f, h * 0.55f),
            cornerRadius = CornerRadius(12f, 12f)
        )
        // External view sky + sun
        drawRoundRect(
            color = Color(0xFFBBDEFB),
            topLeft = Offset(w * 0.47f, h * 0.17f),
            size = Size(w * 0.41f, h * 0.51f),
            cornerRadius = CornerRadius(8f, 8f)
        )
        // Outdoor sun
        drawCircle(
            color = Color(0xFFFFF176).copy(alpha = 0.8f),
            radius = w * 0.06f,
            center = Offset(w * 0.76f, h * 0.28f)
        )

        // Window frames
        drawLine(
            color = Color(0xFF37474F),
            start = Offset(w * 0.675f, h * 0.17f),
            end = Offset(w * 0.675f, h * 0.68f),
            strokeWidth = 6f
        )
        drawLine(
            color = Color(0xFF37474F),
            start = Offset(w * 0.47f, h * 0.4f),
            end = Offset(w * 0.88f, h * 0.4f),
            strokeWidth = 6f
        )

        // Contemporary pendant drop lights
        drawLine(
            color = Color(0xFF212121),
            start = Offset(w * 0.22f, 0f),
            end = Offset(w * 0.22f, h * 0.28f),
            strokeWidth = 4f
        )
        drawCircle(
            color = Color(0xFFF57C00),
            radius = w * 0.04f,
            center = Offset(w * 0.22f, h * 0.3f)
        )
        drawArc(
            color = Color(0xFF37474F),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(w * 0.16f, h * 0.23f),
            size = Size(w * 0.12f, h * 0.08f)
        )

        // Minimalist corporate/domestic sofa
        drawRoundRect(
            color = Color(0xFF455A64), // Sofa chassis
            topLeft = Offset(w * 0.1f, h * 0.55f),
            size = Size(w * 0.45f, h * 0.22f),
            cornerRadius = CornerRadius(16f, 16f)
        )
        // Sofa cushions
        drawRoundRect(
            color = Color(0xFF607D8B),
            topLeft = Offset(w * 0.13f, h * 0.58f),
            size = Size(w * 0.18f, h * 0.14f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
            color = Color(0xFF607D8B),
            topLeft = Offset(w * 0.33f, h * 0.58f),
            size = Size(w * 0.18f, h * 0.14f),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // Sleek Floor boards
        drawLine(
            color = Color(0xFF8D6E63),
            start = Offset(0f, h * 0.77f),
            end = Offset(w, h * 0.77f),
            strokeWidth = 8f
        )
        drawRect(
            color = Color(0xFFD7CCC8),
            topLeft = Offset(0f, h * 0.78f),
            size = Size(w, h * 0.22f)
        )
    }
}

@Composable
fun StructuralDraftingIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Dark grey blueprint analytics background
        drawRect(color = Color(0xFF1E242B))

        // Vector axis grid
        val cX = w * 0.5f
        val cY = h * 0.5f

        // Centered construction pillars cross-sections
        val strokeStyle = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f)))
        drawLine(color = Color.White.copy(alpha = 0.3f), start = Offset(0f, cY), end = Offset(w, cY), strokeWidth = 2f)
        drawLine(color = Color.White.copy(alpha = 0.3f), start = Offset(cX, 0f), end = Offset(cX, h), strokeWidth = 2f)

        // Radial vector circles
        drawCircle(color = Color(0xFF00E676).copy(alpha = 0.25f), radius = h * 0.35f, center = Offset(cX, cY), style = Stroke(width = 2f))
        drawCircle(color = Color(0xFF00E676).copy(alpha = 0.15f), radius = h * 0.2f, center = Offset(cX, cY), style = Stroke(width = 2f))

        // Tension vector arrow lines
        // Main structural load vectors
        val path = Path().apply {
            moveTo(cX, cY)
            lineTo(cX - w * 0.3f, cY - h * 0.3f)
            moveTo(cX, cY)
            lineTo(cX + w * 0.25f, cY - h * 0.28f)
            moveTo(cX, cY)
            lineTo(cX + w * 0.32f, cY + h * 0.25f)
            moveTo(cX, cY)
            lineTo(cX - w * 0.2f, cY + h * 0.32f)
        }
        drawPath(path = path, color = Color(0xFFFFEB3B), style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f))))

        // Anchor points
        drawCircle(color = Color(0xFFFF5252), radius = 10f, center = Offset(cX, cY))
        drawCircle(color = Color(0xFF29B6F6), radius = 8f, center = Offset(cX - w * 0.3f, cY - h * 0.3f))
        drawCircle(color = Color(0xFF29B6F6), radius = 8f, center = Offset(cX + w * 0.25f, cY - h * 0.28f))
        drawCircle(color = Color(0xFF29B6F6), radius = 8f, center = Offset(cX + w * 0.32f, cY + h * 0.25f))
        drawCircle(color = Color(0xFF29B6F6), radius = 8f, center = Offset(cX - w * 0.2f, cY + h * 0.32f))
    }
}

@Composable
fun MaterialSupplyIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Sky / backdrop
        drawRect(color = Color(0xFFF5F5F5))

        // Floor / ground road
        drawRect(color = Color(0xFFBDBDBD), topLeft = Offset(0f, h * 0.72f), size = Size(w, h * 0.28f))

        // Supply Delivery Truck
        val cabX = w * 0.58f
        val cabY = h * 0.35f
        val cabW = w * 0.25f
        val cabH = h * 0.32f

        val cargoX = w * 0.16f
        val cargoY = h * 0.22f
        val cargoW = w * 0.43f
        val cargoH = h * 0.45f

        // 1. Cargo Container in Relief India deep blue
        drawRoundRect(
            color = Color(0xFF0D47A1),
            topLeft = Offset(cargoX, cargoY),
            size = Size(cargoW, cargoH),
            cornerRadius = CornerRadius(10f, 10f)
        )
        // Logistics pattern lines
        for (i in 1..4) {
            val lineX = cargoX + (i * cargoW / 5)
            drawLine(
                color = Color.White.copy(alpha = 0.25f),
                start = Offset(lineX, cargoY + 10f),
                end = Offset(lineX, cargoY + cargoH - 10f),
                strokeWidth = 4f
            )
        }

        // 2. Truck Cab in Accent Crimson Red
        drawRoundRect(
            color = Color(0xFFE53935),
            topLeft = Offset(cabX, cabY),
            size = Size(cabW, cabH),
            cornerRadius = CornerRadius(15f, 15f)
        )
        // Re-align connection corner
        drawRect(
            color = Color(0xFFE53935),
            topLeft = Offset(cabX, cabY + h * 0.1f),
            size = Size(cabW * 0.5f, cabH * 0.7f)
        )

        // Glass window on cab
        drawRoundRect(
            color = Color(0xFFE0F7FA),
            topLeft = Offset(cabX + cabW * 0.35f, cabY + cabH * 0.12f),
            size = Size(cabW * 0.5f, cabH * 0.45f),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // Steel truck chassis bridge
        drawRect(
            color = Color(0xFF37474F),
            topLeft = Offset(w * 0.18f, h * 0.65f),
            size = Size(w * 0.62f, h * 0.08f)
        )

        // Wheels
        drawCircle(color = Color(0xFF212121), radius = h * 0.1f, center = Offset(w * 0.3f, h * 0.72f))
        drawCircle(color = Color(0xFFCFD8DC), radius = h * 0.04f, center = Offset(w * 0.3f, h * 0.72f))

        drawCircle(color = Color(0xFF212121), radius = h * 0.1f, center = Offset(w * 0.48f, h * 0.72f))
        drawCircle(color = Color(0xFFCFD8DC), radius = h * 0.04f, center = Offset(w * 0.48f, h * 0.72f))

        drawCircle(color = Color(0xFF212121), radius = h * 0.1f, center = Offset(w * 0.74f, h * 0.72f))
        drawCircle(color = Color(0xFFCFD8DC), radius = h * 0.04f, center = Offset(w * 0.74f, h * 0.72f))
    }
}

@Composable
fun OfficeGpsMapIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Dark/Light tech map background
        drawRect(color = Color(0xFFECEFF1))

        // Crossroads roads vectors
        val roadW = w * 0.16f
        
        // Horizontal road (Mukhtipath Road)
        drawRect(color = Color.White, topLeft = Offset(0f, h * 0.42f), size = Size(w, roadW))

        // Vertical road (Barhalganj Chauraha/Patna Chauraha)
        drawRect(color = Color.White, topLeft = Offset(w * 0.36f, 0f), size = Size(roadW, h))

        // Side lane road
        drawRect(color = Color.White, topLeft = Offset(w * 0.65f, h * 0.22f), size = Size(w * 0.35f, roadW * 0.7f))

        // Road divider dashes
        val dashedStyle = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f)))
        drawLine(
            color = Color(0xFFFFD54F),
            start = Offset(0f, h * 0.42f + roadW * 0.5f),
            end = Offset(w, h * 0.42f + roadW * 0.5f),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f))
        )
        drawLine(
            color = Color(0xFFFFD54F),
            start = Offset(w * 0.36f + roadW * 0.5f, 0f),
            end = Offset(w * 0.36f + roadW * 0.5f, h),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f))
        )

        // Surrounding landscape grids (buildings/green spaces)
        // Park block
        drawRoundRect(
            color = Color(0xFFA5D6A7),
            topLeft = Offset(w * 0.08f, h * 0.1f),
            size = Size(w * 0.22f, h * 0.25f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        // Civil block
        drawRoundRect(
            color = Color(0xFFCFD8DC),
            topLeft = Offset(w * 0.58f, h * 0.58f),
            size = Size(w * 0.34f, h * 0.32f),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // Local Relief India Complex landmark pin
        val pinX = w * 0.44f
        val pinY = h * 0.4f

        // Draw dynamic locator pulse ring
        drawCircle(
            color = Color(0xFF0D47A1).copy(alpha = 0.3f),
            radius = w * 0.15f,
            center = Offset(pinX, pinY)
        )
        drawCircle(
            color = Color(0xFF0D47A1).copy(alpha = 0.15f),
            radius = w * 0.25f,
            center = Offset(pinX, pinY)
        )

        // The Pin shape
        val pinPath = Path().apply {
            moveTo(pinX, pinY)
            cubicTo(pinX - 25f, pinY - 45f, pinX - 25f, pinY - 65f, pinX, pinY - 65f)
            cubicTo(pinX + 25f, pinY - 65f, pinX + 25f, pinY - 45f, pinX, pinY)
            close()
        }
        drawPath(path = pinPath, color = Color(0xFFE53935))
        // Center dot on pin
        drawCircle(color = Color.White, radius = 8f, center = Offset(pinX, pinY - 48f))
    }
}
