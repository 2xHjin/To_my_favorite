package com.example.tomyfavorite.ui.home

import android.R.attr.type
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
    //화면상의 일시적인 UI 상태 정보는 컴포즈 내부의 rememberPagerState가 스스로 기억하고 관리함
    val pagerState = rememberPagerState(pageCount = { images.size })

    // 파스텔 핑크 테마 배경 상자
    //원래 컴포넌트를 겹치거나 쌓을 때 쓰는 상자
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

@Composable
fun StickerBar(
    modifier: Modifier = Modifier
) {
    // 안드로이드 공식 가이드의 Row를 사용하여 가로 배치를 수행합니다.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        // 스케치처럼 아이콘들이 화면 크기에 맞춰 균등하고 예쁜 간격으로 벌어지도록 설정합니다.
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 다꾸 감성의 부드러운 파스텔 톤이나 포인트 색상들을 조합합니다.
        // 실전에서는 이 자리에 혜진님이 준비한 예쁜 PNG/SVG 에셋(꽃, 리본 등)이 들어갈 것입니다.
        // 우선 구조 확인을 위해 머티리얼 기본 아이콘들로 배치해 둘게요!

        Icon(
            imageVector = Icons.Default.Favorite, // 하트 스티커 역할
            contentDescription = "하트 스티커",
            tint = Color(0xFFFFB6C1), // 연분홍
            modifier = Modifier.size(36.dp)
        )

        Icon(
            imageVector = Icons.Default.Star, // 별 스티커 역할
            contentDescription = "별 스티커",
            tint = Color(0xFFFFE4B5), // 파스텔 노랑
            modifier = Modifier.size(32.dp)
        )

        Icon(
            imageVector = Icons.Default.ThumbUp, // 최고 스티커 역할
            contentDescription = "최고 스티커",
            tint = Color(0xFFAEEEEE), // 파스텔 민트/블루
            modifier = Modifier.size(32.dp)
        )

        Icon(
            imageVector = Icons.Default.Home, // 꽃/집 스티커 역할
            contentDescription = "집 스티커",
            tint = Color(0xFFE6E6FA), // 파스텔 보라
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun RecentPhotosSection(
    recentPhotos: List<String>, // 외부에서 받아올 최근 사진 데이터 리스트 (Stateless)
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // 1. 타이틀 영역: 왼쪽 정렬된 '최근 사진' 텍스트
        Text(
            text = "최근 사진",
            style = TextStyle(
                color = Color(0xFF4A4A4A), // 앱바와 통일한 부드러운 먹색
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp) // 좌측 여백 매칭
        )

        // 2. 가로 스크롤 리스트 영역: LazyRow
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            // 양 옆에 여백을 주어 스크롤할 때 자연스럽게 카드들이 화면 끝까지 밀리도록 설정합니다.
            contentPadding = PaddingValues(horizontal = 24.dp),
            // 카드와 카드 사이의 아기자기한 간격 지정
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 외부에서 넘겨받은 사진 리스트를 기반으로 항목(Item)들을 그려냅니다.
            items(recentPhotos) { photo ->
                // 피그마 스펙처럼 모서리가 아주 둥근 사각형 카드 형태
                Card(
                    shape = RoundedCornerShape(16.dp), // 아주 둥근 모서리 사각형
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.size(width = 100.dp, height = 100.dp) // 아기자기한 정사각형 카드 크기
                ) {
                    // 실제 이미지(Coil 라이브러리)가 들어갈 영역의 임시 상자
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5)) // 연한 그레이 톤으로 영역 가시화
                    ) {
                        // 나중에 여기에 AsyncImage(model = photo, ...)가 탑재됩니다!
                    }
                }
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun HomeTopAppBarPreview() {
    // 테마가 있다면 감싸주면 더 정확하게 보입니다!
    HomeTopAppBar(onMenuClick = {})
}



 */



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockImages = listOf("photo1", "photo2", "photo3")
    val mockRecentPhotos = listOf("rec1", "rec2", "rec3") // 최근 사진 가짜 데이터

    Column(modifier = Modifier.fillMaxSize()) {
        HomeTopAppBar(onMenuClick = {}) // 1번 조각 (앱바)
        FavoriteImagePager(images = mockImages) // 2번 조각 (카드 안으로 들어간 슬라이더)
        StickerBar() // 3번 조각 (Row 기반 스티커 영역)

        // ★ [추가] 스티커 바와 최근 사진 사이를 채우는 연한 회색 선
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp), // 양옆 여백을 주어 선이 너무 꽉 차지 않게 예쁘게 조절
            thickness = 1.dp, // 선의 두께를 아주 가늘고 섬세하게 1.dp로 지정
            color = Color( 0xFFE0E0E0) // 다꾸 감성을 해치지 않는 은은하고 연한 회색
        )

        RecentPhotosSection(recentPhotos = mockRecentPhotos) // 4번 조각 추가!
    }
}


/*
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

 */