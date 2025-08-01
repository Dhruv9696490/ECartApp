package com.example.ecartapp.componentsview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun BannerView(modifier: Modifier= Modifier){
    var bannerItem by remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(Unit){
        Firebase.firestore.collection("data")
            .document("banner").get()
            .addOnCompleteListener {
                bannerItem = it.result.get("urls") as List<String>
            }
    }
    val state = rememberPagerState(initialPage = 0, pageCount = { bannerItem.size })
    LaunchedEffect(bannerItem) {
        if (bannerItem.isNotEmpty()) {
            while (true) {
                kotlinx.coroutines.delay(2000L)
                val nextPage = (state.currentPage + 1) % bannerItem.size
                state.animateScrollToPage(nextPage)
            }
        }
    }

        Column(modifier){
            HorizontalPager(state =state, contentPadding = PaddingValues(vertical = 12.dp)){ it->
                AsyncImage(model = if (bannerItem.size > 0) bannerItem.get(it) else "", "Image",
                    modifier = Modifier.fillMaxWidth().clip(
                        RoundedCornerShape(16.dp)
                    ))
            }
            Spacer(modifier = Modifier.height(4.dp))
            DotsIndicator(
                dotCount = bannerItem.size,
                type = ShiftIndicatorType(
                    dotsGraphic = DotGraphic(
                        color = MaterialTheme.colorScheme.primary,
                        size = 6.dp
                    )
                ),
                pagerState = state
            )
        }
}