package com.example.tomyfavorite.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onMenuClick: () -> Unit // 햄버거 버튼 클릭 이벤트를 상위로 전달하는 람다식
) {
    // 다꾸 감성을 위해 타이틀이 중앙에 오는 CenterAlignedTopAppBar를 사용했습니다.
    // 만약 완전히 왼쪽에 붙이고 싶다면 TopAppBar를 사용하면 됩니다.
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "나의 최애에게",
                style = TextStyle(
                    color = Color(0xFF4A4A4A), // 부드러운 글자 색상 (Hex: 4A4A4A)
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                    // 나중에 여기 가독성 좋은 다꾸용 폰트(fontFamily)를 추가할 수 있어요!
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu, // 햄버거 모양 아이콘
                    contentDescription = "메뉴 열기",
                    tint = Color(0xFF4A4A4A) // 아이콘 색상도 글자색과 통일
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFFFE4E1) // 은은한 파스텔 핑크 배경색 (Hex: FFE4E1)
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteImagePager(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    // 파스텔 핑크 테마 배경 상자
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFFE4E1)) // PinkPastel
            .padding(vertical = 16.dp, horizontal = 24.dp)
    ) {
        // ★ 1. 둥근 카드 전체를 감싸는 바깥쪽 Card입니다.
        Card(
            shape = RoundedCornerShape(24.dp), // 피그마처럼 아주 둥글게 깎음
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f) // 아기자기한 비율 유지
        ) {
            // ★ 2. 카드 내부에서 '사진'과 '인디케이터'를 위아래로 겹치기 위해 Box를 씁니다.
            Box(modifier = Modifier.fillMaxSize()) {

                // [층 구조: 1층] 가로로 넘어가는 사진 페이저
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray) // 실제 최애 사진이 채워질 영역
                    )
                }

                // [층 구조: 2층] 사진 위에 둥둥 떠 있는 인디케이터 점들
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // ★ 카드의 하단 중앙에 강제로 배치!
                        .padding(bottom = 16.dp) // 카드 밑바닥에서 살짝 위로 띄움
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(images.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Color(0xFF4A4A4A) else Color(0xFF4A4A4A).copy(alpha = 0.3f)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopAppBarPreview() {
    // 테마가 있다면 감싸주면 더 정확하게 보입니다!
    HomeTopAppBar(onMenuClick = {})
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // 가상의 테스트용 사진 경로 리스트 3개를 임의로 넣어봅니다.
    val mockImages = listOf("photo1", "photo2", "photo3")

    Column(modifier = Modifier.fillMaxSize()) {
        HomeTopAppBar(onMenuClick = {}) // 1번 조각
        FavoriteImagePager(images = mockImages) // 2번 조각
    }
}