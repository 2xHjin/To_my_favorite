package com.example.tomyfavorite.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.tomyfavorite.R


/*
sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object Home : BottomNavItem("home", R.drawable.ic_home, "홈")
    object Gallery : BottomNavItem("gallery", R.drawable.ic_gallery, "갤러리")
    object Memo : BottomNavItem("memo", R.drawable.ic_memo, "최애체크")
}


 */

// 탭의 종류를 안전하게 관리하기 위해 선언한 Enum Class입니다. (SoC 구조)
enum class HomeTab(val titleId: Int, val icon: ImageVector) {
    HOME( R.string.home_tab_title_home, Icons.Default.Home),
    MEMO(R.string.home_tab_title_memo, Icons.AutoMirrored.Filled.List),
    SETTINGS(R.string.home_tab_title_settings, Icons.Default.Settings)
}