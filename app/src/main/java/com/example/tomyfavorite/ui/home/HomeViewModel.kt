package com.example.tomyfavorite.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. 화면에 뿌려질 모든 데이터들을 묶어놓은 '데이터 보따리' (상태 클래스)
data class HomeUiState(
    val images: List<String> = emptyList(),
    val recentPhotos: List<String> = emptyList(),
    val recentMemos: List<MemoSummary> = emptyList()
)

class HomeViewModel : ViewModel() {

    // 2. ViewModel 내부에서만 값을 수정할 수 있는 실제 금고 (_uiState)
    private val _uiState = MutableStateFlow(HomeUiState())

    // 3. UI(화면)에게는 읽기 권한만 주는 진열장 (uiState)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // ViewModel이 생성되자마자 가짜 데이터를 불러와서 금고에 채워 넣습니다.
        loadMockData()
    }

    private fun loadMockData() {
        _uiState.value = HomeUiState(
            images = listOf("photo1", "photo2", "photo3"),
            recentPhotos = listOf("rec1", "rec2", "rec3"),
            recentMemos = listOf(
                MemoSummary("오늘의 최애 스케줄", "엠카방송 본방사수하기! 잊지 말고 투표도 꼭 참여하자."),
                MemoSummary("덕질 일기", "오늘 올라온 비하인드 포토 카드 퀄리티 진짜 역대급이다 ㅠㅠ"),
                MemoSummary("구매 리스트", "시즌 그리팅 앨범 패키지 B세트 공동구매 수량 확인 필요")
            )
        )
    }
}