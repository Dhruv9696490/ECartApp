package com.example.ecartapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecartapp.GlobalNavigation
import com.example.ecartapp.Utils
import com.example.ecartapp.model.CategoryModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun CategoryItem(modifier: Modifier, id: String){
    var categoryData by remember { mutableStateOf<List<CategoryModel>>(emptyList()) }
    LaunchedEffect(Unit){
        Firebase.firestore.collection("data").document("icons")
            .collection("products")
            .whereEqualTo("category",id)
            .get().addOnCompleteListener{
                if(it.isSuccessful){
                    val categoryDatas = it.result.documents.mapNotNull {item->
                        item.toObject(CategoryModel::class.java)
                    }
                    categoryData = categoryDatas.plus(categoryDatas).plus(categoryDatas).plus(categoryDatas)
                }

            }
    }
    LazyVerticalGrid(GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)){
        items(categoryData){it->
            CategoryItemsScreen(it)
        }
    }
}
@Composable
fun CategoryItemsScreen(item: CategoryModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(200.dp,300.dp)
            .clickable(onClick = {
                GlobalNavigation.navController.navigate("item-screen/" + item.id)
            }),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ){
        Box(){
            IconButton(onClick = {}, modifier = Modifier.size(60.dp).align(alignment = Alignment.TopEnd)){
                Icon(Icons.Default.FavoriteBorder,null)
            }
            Column(
                modifier = Modifier.padding(top = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                AsyncImage(
                    item.image.firstOrNull(), null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Text(
                    item.name,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("₹ ${item.realprice}", color = Color.DarkGray)
                    IconButton(onClick = {
                        Utils.addToCart(context, item.id)
                    }) {
                        Icon(Icons.Default.ShoppingCart, null)
                    }

                }

            }
        }
    }
}

@Composable
fun ItemScreen(modifier: Modifier,id: String){
    val context = LocalContext.current
    var category: CategoryModel? by remember { mutableStateOf(CategoryModel()) }
    LaunchedEffect(Unit){
        Firebase.firestore.collection("data").document("icons")
            .collection("products").document(id).get().addOnCompleteListener {
                category=it.result.toObject(CategoryModel::class.java)
            }
    }
    val state = rememberPagerState(initialPage = 0, pageCount = { category?.image?.size ?: 0 })
    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(state = rememberScrollState())) {

        HorizontalPager(
            state = state, contentPadding = PaddingValues(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentSize(),
            pageSpacing = 34.dp
        ) { it ->
            AsyncImage(
                model = category?.image?.get(it), null,
                modifier = Modifier
                    .height(320.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        DotsIndicator(
            dotCount = category?.image?.size ?: 0,
            type = ShiftIndicatorType(
                dotsGraphic = DotGraphic(
                    color = MaterialTheme.colorScheme.primary,
                    size = 8.dp
                )
            ),
            pagerState = state
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(category?.name!!, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif)
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            Text("₹${category?.price}" ,
                textDecoration = TextDecoration.LineThrough,
                fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "₹${category?.realprice!!}", color = Color.DarkGray,
                fontSize = 23.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}){
                Icon(Icons.Default.FavoriteBorder,null)
            }
        }
        Button(onClick = {
            Utils.addToCart(context,id)
        }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)){
            Text("Add To Cart", fontSize = 16.sp)
        }
        Spacer(Modifier.height(4.dp))
        if (category!!.otherdetails.isNotEmpty()){
            Text(
                "Other Details-",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(Modifier.height(4.dp))
            category?.otherdetails?.mapNotNull {
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    ){
                        append((it.key+": "))
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 18.sp
                        )
                    ){
                        append(it.value)
                    }
                })
            }
        }
        Spacer(Modifier.height(4.dp))
       Text("Description-", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif)
        Spacer(Modifier.height(4.dp))
        Text(category?.description!!,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily.SansSerif,
            fontSize = 16.sp)
    }
}