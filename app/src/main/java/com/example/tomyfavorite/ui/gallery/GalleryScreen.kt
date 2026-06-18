package com.example.tomyfavorite.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomyfavorite.ui.theme.SoftDarkCharcoal
import com.example.tomyfavorite.ui.theme.SoftGrayDivider
import coil.compose.AsyncImage
import com.example.tomyfavorite.ui.theme.TomyfavoriteTheme


// ==========================================
// 2. Stateful Screen (최상위: 데이터 구독 및 이벤트 위임)
// ==========================================
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = viewModel(),
    onFolderClick: (String) -> Unit // NavHost로 이벤트를 쏘아 올릴 바통
) {
    // 뷰모델의 데이터를 실시간으로 관찰
    val folders by viewModel.uiState.collectAsState()

    // 상태가 없는 그림쟁이 컴포저블 호출
    GalleryContent(
        folders = folders,
        onFolderClick = onFolderClick
    )
}

// ==========================================
// 3. Stateless Content (순수 UI 렌더링 영역)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryContent(
    folders: List<FolderUiState>,
    onFolderClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 이미 바텀바는 MainApp에 있으므로, 여기 Scaffold는 상단바 전용으로 작동합니다.
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "갤러리",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = SoftDarkCharcoal
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White,
        modifier = modifier
    ) { innerPadding ->
        // 4열 그리드 레이아웃
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // TopAppBar가 차지한 높이만큼 안전하게 내려옵니다.
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(folders, key = { it.id }) { folder ->
                FolderItem(
                    folder = folder,
                    onClick = { onFolderClick(folder.id) }
                )
            }
        }
    }
}

// ==========================================
// 4. Component (단일 폴더 아이템)
// ==========================================
@Composable
fun FolderItem(
    folder: FolderUiState,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
// 💡 여기를 최신 Compose Ripple 충돌 방지 및 다꾸 감성 전용 코드로 대체합니다!
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // 클릭 시 투박한 회색 번쩍임(리플)을 꺼서 깔끔한 다꾸 감성을 유지합니다.
            ) {
                onClick()
            }
        ,horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 모서리가 둥근 폴더 커버 이미지
        AsyncImage(
            model = folder.thumbnailUrl,
            contentDescription = folder.name,
            modifier = Modifier
                .aspectRatio(1f) // 정사각형 비율 고정
                .clip(RoundedCornerShape(16.dp))
                .background(SoftGrayDivider),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 폴더 타이틀
        Text(
            text = folder.name,
            style = TextStyle(
                fontSize = 14.sp,
                color = SoftDarkCharcoal,
                textAlign = TextAlign.Center
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ==========================================
// 5. Preview (프리뷰 렌더링 전용)
// ==========================================
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun GalleryContentPreview() {
    // 1. 프리뷰를 위한 가짜 폴더 데이터 보따리 생성
    val fakeFolders = listOf(
        FolderUiState(id = "1", name = "폴더 1", thumbnailUrl = "https://example.com/img1.jpg"),
        FolderUiState(id = "2", name = "폴더 2", thumbnailUrl = "https://example.com/img2.jpg"),
        FolderUiState(id = "3", name = "폴더 3", thumbnailUrl = "https://example.com/img3.jpg"),
        FolderUiState(id = "4", name = "폴더 4", thumbnailUrl = "https://example.com/img4.jpg"),
        FolderUiState(id = "5", name = "폴더 5", thumbnailUrl = "https://example.com/img5.jpg")
    )

    // 2. 혜진님 앱의 메인 테마를 씌워서 렌더링 (테마명은 실제 프로젝트에 맞게 수정해주세요)
    TomyfavoriteTheme {
        // 3. ViewModel이 연결된 GalleryScreen이 아닌, 그림쟁이 GalleryContent를 바로 호출!
        GalleryContent(
            folders = fakeFolders,
            onFolderClick = { clickedId ->
                // 프리뷰에서는 클릭해도 다른 화면으로 넘어갈 필요가 없으니 빈 람다로 둡니다.
                println("Preview Folder Clicked: $clickedId")
            }
        )
    }
}