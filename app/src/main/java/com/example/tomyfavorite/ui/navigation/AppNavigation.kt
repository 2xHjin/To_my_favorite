package com.example.tomyfavorite.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tomyfavorite.ui.gallery.GalleryScreen
import com.example.tomyfavorite.ui.home.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // 1. NavHost: 화면들이 전환되는 마법의 무대입니다. 기본 시작 화면은 'HOME'으로 지정합니다.
    NavHost(
        navController = navController,
        startDestination = HomeTab.HOME.name,
        modifier = modifier
    ) {
        // 2. 홈 화면 경로 정의
        composable(route = HomeTab.HOME.name) {
            HomeScreen()
        }

        // 3. 메모장 화면 경로 정의 (임시 뼈대)
        composable(route = HomeTab.MEMO.name) {
            DummyMemoScreen()
        }

        // 4. 설정 화면 경로 정의 (임시 뼈대)
        composable(route = HomeTab.GALLERY.name) {
//DummySettingsScreen()
            GalleryScreen(
                onFolderClick = { folderId ->
                    // 나중에 여기서 상세 그리드 화면(FolderDetailScreen)으로 이동하는 로직을 작성합니다.
                    navController.navigate("folder_detail/$folderId")
                }
            )
        }
    }
}

// ==========================================
// 임시 화면 컴포저블 (나중에 혜진님이 멋지게 채워넣을 공간!)
// ==========================================
@Composable
fun DummyMemoScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "🌸 최애 메모장 화면 (준비 중) 🌸")
    }
}

@Composable
fun DummySettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "⚙️ 설정 화면 (준비 중) ⚙️")
    }
}