package com.example.kotlinpaging

import kotlinx.coroutines.delay

class DummyRepository {
    // TODO: Replace with ROOM
    suspend fun loadItems(page: Int, pageSize: Int): Result<List<DummyItem>> {
        delay(2000L)
        val startingIndex = page * pageSize
        return if(startingIndex + pageSize <= remoteDataSource.size) {
            Result.success(remoteDataSource.slice(startingIndex until startingIndex + pageSize))
        } else Result.success(emptyList())
    }

    // TODO: Replace with DataSource
    private val remoteDataSource = (1..100).map {
        DummyItem(
            title = "Item $it",
            description = "Description $it"
        )
    }
}
