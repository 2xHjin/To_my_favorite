package com.example.tomyfavorite.ui

import android.net.http.SslCertificate.restoreState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tomyfavorite.ui.home.HomeCustomBottomBar
import com.example.tomyfavorite.ui.navigation.AppNavHost
import com.example.tomyfavorite.ui.navigation.HomeTab

@Composable
fun MainApp() {
    // 1. 앱 전체의 네비게이션 제어권을 가진 든든한 조종사(navController) 생성
    val navController = rememberNavController()

    // 2. 현재 네비게이션 시스템이 어떤 화면을 가리키고 있는지 실시간으로 관찰(Observe)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: HomeTab.HOME.name

    // 문자열로 된 경로(Route)를 우리가 만든 안전한 HomeTab Enum으로 매칭 변환
    val currentTab = HomeTab.values().find { it.name == currentRoute } ?: HomeTab.HOME

    Scaffold(
        bottomBar = {
            // 3. 우리가 만든 반원형 커스텀 바텀바 배치!
            HomeCustomBottomBar(
                currentTab = currentTab,
                onTabClick = { selectedTab ->
                    // 💡 핵심: 바텀바 아이템이 눌리면, 조종사에게 해당 화면으로 이동하라고 명령을 내립니다!
                    navController.navigate(selectedTab.name) {
                        // 중복 화면 쌓임 방지 및 백스택 청소를 위한 정석 실무 옵션
                        popUpTo(HomeTab.HOME.name) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        // 4. 바텀바를 제외한 중앙 무대에 네비게이션 호스트를 올립니다.
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

