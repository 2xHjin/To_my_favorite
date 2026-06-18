package com.example.tomyfavorite.ui.gallery

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 상태 데이터 보따리 명세서
data class FolderUiState(
    val id: String,
    val name: String,
    val thumbnailUrl: String
)

// 실제 데이터를 관리하는 금고
class GalleryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<List<FolderUiState>>(emptyList())
    val uiState: StateFlow<List<FolderUiState>> = _uiState.asStateFlow()

    init {
        _uiState.value = listOf(
            FolderUiState("1", "폴더 1", "https://picsum.photos/seed/folder1/400/400"),
            FolderUiState("2", "폴더 2", "https://picsum.photos/seed/folder2/400/400"),
            FolderUiState("3", "폴더 3", "https://example.com/image3.jpg"),
            FolderUiState("4", "폴더 4", "https://picsum.photos/seed/folder4/400/400")
        )
    }
}