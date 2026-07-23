package com.quickbill.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class QuickBillColors(
    val background: Color,
    val paper: Color,
    val paperYellow: Color,
    val accent: Color,
    val overdue: Color,
    val steel: Color,
    val wood: Color,
    val onPaper: Color
)

val LocalQuickBillColors = staticCompositionLocalOf {
    QuickBillColors(
        background = Graphite,
        paper = PaperOffWhite,
        paperYellow = PaperYellow,
        accent = CoolBlue,
        overdue = AmberOverdue,
        steel = Steel,
        wood = Wood,
        onPaper = Color(0xFF1C1C1E)
    )
}

@Composable
fun QuickBillTheme(
    content: @Composable () -> Unit
) {
    val colors = QuickBillColors(
        background = Graphite,
        paper = PaperOffWhite,
        paperYellow = PaperYellow,
        accent = CoolBlue,
        overdue = AmberOverdue,
        steel = Steel,
        wood = Wood,
        onPaper = Color(0xFF1C1C1E)
    )

    CompositionLocalProvider(LocalQuickBillColors provides colors) {
        content()
    }
}

object QuickBillTheme {
    val colors: QuickBillColors
        @Composable
        get() = LocalQuickBillColors.current
}
