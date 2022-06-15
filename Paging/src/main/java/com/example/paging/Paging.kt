package com.example.paging
/**
 * Contract.
 */
interface PageContract<Key,Item> {
    suspend fun loadNextItems()
    fun reset()
}
/**
 * Class to handle all the logic to load the data from local dB.
 */
class PageMediator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val onNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) : PageContract<Key, Item> {

    private var currentKey = initialKey
    private var loading = false

    override suspend fun loadNextItems() {
        if (loading) return
        loading = true
        onLoadUpdated(true)
        loading = false
        val items: List<Item> = onRequest(currentKey).getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentKey = onNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}
