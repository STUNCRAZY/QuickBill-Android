package com.quickbill.feature.counter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quickbill.core.ui.theme.QuickBillTheme

/**
 * The heart of QuickBill – the physical shop counter.
 * Order pad (top) + ticket rail (middle) + cash drawer (bottom).
 * Full-bleed, zero chrome.
 */
@Composable
fun ShopCounterScreen(
    modifier: Modifier = Modifier
) {
    val colors = QuickBillTheme.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // ORDER PAD (top ~28%)
        OrderPadPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.28f)
                .padding(12.dp)
        )

        // SEARCH field integrated
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF2C2C2E)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "  who hasn\'t paid me?",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        // TICKET RAIL (middle ~50%)
        TicketRailPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.50f)
                .padding(horizontal = 8.dp)
        )

        // CASH DRAWER (bottom ~22%)
        CashDrawerPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.22f)
                .padding(12.dp)
        )
    }
}

@Composable
private fun OrderPadPlaceholder(modifier: Modifier = Modifier) {
    val colors = QuickBillTheme.colors
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(colors.paper)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "ORDER PAD",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = colors.onPaper.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(8.dp))
            Text("Customer: ____________________", fontSize = 14.sp, color = colors.onPaper)
            Text("Job: _________________________", fontSize = 14.sp, color = colors.onPaper)
            Text("Amount: _____________________", fontSize = 14.sp, color = colors.onPaper)
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.accent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tear \u2192 Send",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TicketRailPlaceholder(modifier: Modifier = Modifier) {
    val colors = QuickBillTheme.colors
    Box(
        modifier = modifier
            .background(Color(0xFF252528))
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = "TICKET RAIL — who owes me",
                color = Color.Gray,
                fontSize = 11.sp
            )
            Spacer(Modifier.height(8.dp))
            // Placeholder tickets
            listOf(
                "Ramirez — Fence repair — $390 — today",
                "Nguyen — Panel upgrade — $2,100 — 6 days",
                "Torres HVAC — $1,240 — 21 days · REMIND"
            ).forEach { ticket ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (ticket.contains("REMIND")) colors.overdue.copy(alpha = 0.3f) else colors.paper)
                        .padding(10.dp)
                ) {
                    Text(ticket, fontSize = 13.sp, color = colors.onPaper)
                }
            }
        }
    }
}

@Composable
private fun CashDrawerPlaceholder(modifier: Modifier = Modifier) {
    val colors = QuickBillTheme.colors
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(colors.wood)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "CASH DRAWER",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
            Text(
                text = "$7,410 this month",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
