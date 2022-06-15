package com.example.kotlinpaging

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paging.PageMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val dummyRepository = DummyRepository()

    init {
        // TODO: Remover esto
        viewModelScope.launch {
            delay(200)
            loadNextItems()
        }
    }

    var state by mutableStateOf(ScreenState())

    // TODO: trasladar esté método a un Repo e inyectarlo.
    private val mediator = PageMediator(
        initialKey = state.page,
        onLoadUpdated = { state = state.copy(isLoading = it) },
        onRequest = { nextPage -> dummyRepository.loadItems(nextPage, PAGE_SIZE) },
        onNextKey = { state.page + 1 },
        onError =  { state = state.copy(error =  it?.localizedMessage) },
        onSuccess = { items, newKey ->
            state = state.copy(
                items = state.items + items,
                page = newKey,
                endOfPagination = items.isEmpty()
            )
        }
    )

    fun loadNextItems() {
        viewModelScope.launch {
            mediator.loadNextItems()
        }
    }

    companion object {
        const val PAGE_SIZE = 25
        const val PRE_SIZE = 10
    }
}

data class ScreenState(
    val isLoading: Boolean = false,
    val items: List<DummyItem> = emptyList(),
    val error: String? = null,
    val endOfPagination: Boolean = false,
    val page: Int = 0
)
