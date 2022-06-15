package com.example.kotlinpaging

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinpaging.MainViewModel.Companion.PRE_SIZE

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val viewModel = viewModel<MainViewModel>()
                val scrollState = rememberLazyListState()
                val state = viewModel.state
                // TODO: ADD or Create a Swipe Refresh
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.items.size) { i ->
                        val item = state.items[i]
                        LaunchedEffect(scrollState) {
                            // TODO: Llevar dentro del ViewModel esta lógica
                            if (
                                i > state.items.size - PRE_SIZE &&
                                !state.endOfPagination &&
                                !state.isLoading
                            ) {
                                viewModel.loadNextItems()
                            }
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(text = item.title, fontSize = 20.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = item.description)
                        }
                    }
                    item {
                        if (state.isLoading) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(9.dp)
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
