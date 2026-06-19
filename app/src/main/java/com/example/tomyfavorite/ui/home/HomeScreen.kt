package com.example.tomyfavorite.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tomyfavorite.R
import com.example.tomyfavorite.ui.navigation.HomeTab
import com.example.tomyfavorite.ui.theme.LavenderBlush
import com.example.tomyfavorite.ui.theme.PastelPink
import com.example.tomyfavorite.ui.theme.SoftCreamPink
import com.example.tomyfavorite.ui.theme.SoftDarkCharcoal
import com.example.tomyfavorite.ui.theme.SoftGrayDivider

@Composable
fun HomeSectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),// 양옆 여백을 주어 선이 너무 꽉 차지 않게 예쁘게 조절
        thickness = 1.dp,// 선의 두께를 아주 가늘고 섬세하게 1.dp로 지정
        color = SoftGrayDivider// 다꾸 감성을 해치지 않는 은은하고 연한 회색
    )
}


// 기존 탭 Enum은 그대로 유지하되, 구조 결합용으로 사용합니다.
enum class CustomTab { MEMO, SETTINGS }


// 1. Stateful Screen (최상위: ViewModel 금고 관리)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = HomeViewModel() // 뷰모델 주입
) {
    // 뷰모델의 금고(StateFlow)를 실시간 관찰하여 uiState 보따리로 변환
    val uiState by viewModel.uiState.collectAsState()

    // 그림쟁이 컴포저블(HomeContent)에게 모든 데이터를 토스!
    HomeContent(uiState = uiState)
}

@Composable
fun HomeContent(
    uiState: HomeUiState,                 // ◀ 여기서 뷰모델의 데이터 보따리를 받음!
    modifier: Modifier = Modifier
) {
    // 안드로이드 공식 레이아웃 뼈대인 Scaffold를 사용하여
    // 상단바를 완벽한 정석 위치에 고정합니다.
    Scaffold(
        topBar = {
            HomeTopAppBar(onMenuClick = { /* 메뉴 제어 로직 */ })
        },
        containerColor = Color.Transparent, // ◀ 핵심: Scaffold 배경을 투명하게 해서 뒤가 비치게 함!
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.padding(innerPadding)
                .padding(top = innerPadding.calculateTopPadding()) // 상단만 적용
                .verticalScroll(rememberScrollState()) // 세로 스크롤 장착
        ) {
            // ★ 핵심: 보따리(uiState)에서 각 조각에 맞는 알맹이 리스트만 쏙쏙 꺼내서 주입합니다!
            FavoriteImagePager(images = uiState.images)

            StickerBar()

            HomeSectionDivider()

            RecentPhotosSection(recentPhotos = uiState.recentPhotos)

            HomeSectionDivider()

            // 최근 메모 영역에 데이터 주입
            RecentMemosSection(recentMemos = uiState.recentMemos)
        }
    }
}

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
                text = stringResource(id = R.string.home_appbar_title),
                style = TextStyle(
                    color = SoftDarkCharcoal, // 부드러운 글자 색상 (Hex: 4A4A4A) 먹색
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
                    contentDescription = stringResource(id = R.string.home_appbar_cd_menu),
                    tint = SoftDarkCharcoal // 아이콘 색상도 글자색과 통일
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = PastelPink // 은은한 파스텔 핑크 배경색 (Hex: FFE4E1)
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
            .background(PastelPink) // PinkPastel
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
                                    if (isSelected) SoftDarkCharcoal else SoftDarkCharcoal.copy(alpha = 0.3f)
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
            .padding(vertical = 32.dp, horizontal = 16.dp),
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
            text = stringResource(id = R.string.home_title_recent_photos),
            style = TextStyle(
                color = SoftDarkCharcoal, // 앱바와 통일한 부드러운 먹색
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


// 외부 레이어와의 SoC(관심사 분리)를 위해 메모 데이터를 표현할 간단한 가짜 데이터 구조를 정의합니다.
data class MemoSummary(
    val title: String,
    val content: String
)

@Composable
fun RecentMemosSection(
    recentMemos: List<MemoSummary>, // 외부에서 주입받는 Stateless 구조
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // 1. 타이틀 영역: '최근 메모' 텍스트 (최근 사진 타이틀과 좌측 여백 통일)
        Text(
            text = stringResource(id = R.string.home_title_recent_memos),
            style = TextStyle(
                color = SoftDarkCharcoal, // 부드러운 먹색
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
        )

        // 2. 카드 뉴스 형태의 가로 스크롤 영역: LazyRow
        LazyRow(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp),

            contentPadding = PaddingValues(horizontal = 24.dp), // 스크롤 시 여백 매칭
            horizontalArrangement = Arrangement.spacedBy(12.dp) // 카드 간의 아기자기한 간격
        ) {
            items(recentMemos) { memo ->
                // 다이어리 속지 느낌을 내기 위해 아주 연한 크림 핑크 톤의 투명도 높은 카드 배치
                Card(
                    shape = RoundedCornerShape(16.dp), // 모서리가 둥근 사각형
                    colors = CardDefaults.cardColors(
                        containerColor = SoftCreamPink // 아주 연한 핑크 화이트 크림 색상
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.size(width = 130.dp, height = 130.dp) // 정사각형에 가까운 비율
                ) {
                    // 카드 안쪽 텍스트 내용들이 위아래로 깔끔하게 정렬되는 배치
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween // 제목은 위, 내용은 아래로 균형 있게 분산
                    ) {
                        // 메모 제목
                        Text(
                            text = memo.title,
                            style = TextStyle(
                                color = Color(0xFF333333),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1, // 제목이 길어지면 잘리도록 방어 코드
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // 메모 내용 일부 (카드 뉴스 본문 역할)
                        Text(
                            text = memo.content,
                            style = TextStyle(
                                color = Color(0xFF666666),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            ),
                            maxLines = 3, // 정사각형 상자를 넘치지 않게 최대 3줄로 제한
                            overflow = TextOverflow.Ellipsis // 3줄 넘어가면 말줄임표(...) 표시
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeCustomBottomBar(
    currentTab: HomeTab,
    onTabClick: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    // 💡 마법의 1줄: 현재 핸드폰의 시스템 하단바(소프트키) 높이를 동적으로 측정합니다!
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        // 1. 배경이 되는 바텀 앱바
        BottomAppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            containerColor = PastelPink,
            tonalElevation = 0.dp,
            // 💡 핵심 1: 다시 기본 인셋을 허용하여, 핑크색 배경이 시스템 소프트키 영역까지 꽉 채우도록 만듭니다!
            windowInsets = WindowInsets.navigationBars
        ) {
            val isGallerySelected = currentTab == HomeTab.GALLERY
            IconButton(
                onClick = { onTabClick(HomeTab.GALLERY) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = HomeTab.GALLERY.icon),
                    contentDescription = stringResource(id = HomeTab.GALLERY.titleId ?: 0),
                    tint = if (isGallerySelected) SoftDarkCharcoal else SoftDarkCharcoal.copy(alpha = 0.4f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            val isMemoSelected = currentTab == HomeTab.MEMO
            IconButton(
                onClick = { onTabClick(HomeTab.MEMO) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = HomeTab.MEMO.icon),
                    contentDescription = stringResource(id = HomeTab.MEMO.titleId ?: 0),
                    tint = if (isMemoSelected) SoftDarkCharcoal else SoftDarkCharcoal.copy(alpha = 0.4f)
                )
            }
        }

        // 2. ★ 주인공: 반원형 홈 버튼
        val isHomeSelected = currentTab == HomeTab.HOME
        FloatingActionButton(
            onClick = { onTabClick(HomeTab.HOME) },
            // 💡 핵심 2: 핑크 바텀바가 소프트키만큼 높아졌으니, 홈 버튼도 (기본 띄움 45dp + 소프트키 높이)만큼 공중으로 더 끌어올립니다!
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = -(45.dp + navBarHeight))
                .size(60.dp),
            shape = CircleShape,
            containerColor = if (isHomeSelected) LavenderBlush else PastelPink,
            contentColor = SoftDarkCharcoal,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = HomeTab.HOME.icon),
                contentDescription = stringResource(id = HomeTab.HOME.titleId ?: 0),
                modifier = Modifier.size(30.dp)
            )
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

    // 프리뷰에서는 ViewModel을 생성하지 않고, 직접 원하는 가짜 보따리를 조립합니다.
    val fakeUiState = HomeUiState(
        images = listOf("photo1", "photo2", "photo3"),
        recentPhotos = listOf("rec1", "rec2", "rec3"),
        recentMemos = listOf(
            MemoSummary("프리뷰 제목 1", "안드로이드 스튜디오 프리뷰에서 보여질 가짜 내용입니다."),
            MemoSummary("프리뷰 제목 2", "UI 레이아웃이 예쁘게 깨지지 않고 나오는지 테스트 중!"),
            MemoSummary("프리뷰 제목 3", "UI 레이아웃이 예쁘게 깨지지 않고 나오는지 테스트 중!"),
            MemoSummary("프리뷰 제목 4", "신기하다 신기해!")
            )

    )


    var fakeTab by remember { mutableStateOf(HomeTab.HOME) }

    // 뷰모델 연결이 없는 HomeContent를 바로 호출하므로 빨간 줄 없이 실시간 디자인 확인 가능!
    HomeContent(uiState = fakeUiState)
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