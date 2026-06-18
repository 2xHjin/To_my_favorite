package com.example.tomyfavorite.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import com.example.tomyfavorite.R


/*
sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object Home : BottomNavItem("home", R.drawable.ic_home, "홈")
    object Gallery : BottomNavItem("gallery", R.drawable.ic_gallery, "갤러리")
    object Memo : BottomNavItem("memo", R.drawable.ic_memo, "최애체크")
}


 */

// 탭의 종류를 안전하게 관리하기 위해 선언한 Enum Class입니다. (SoC 구조)
enum class HomeTab(val titleId: Int, val icon: Int) {
    HOME( R.string.home_tab_title_home, R.drawable.ic_home),
    MEMO(R.string.home_tab_title_memo, R.drawable.ic_memo),
    GALLERY(R.string.home_tab_title_gallery,  R.drawable.ic_gallery)
}