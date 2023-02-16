@file:OptIn(ExperimentalTime::class)

package info.hauu.highloadsocial.config

import com.fasterxml.jackson.annotation.JsonProperty
import info.hauu.highloadsocial.service.PostCacheService
import mu.KotlinLogging
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private val logger = KotlinLogging.logger {}
const val POST_UPDATE_CACHE = "friendPosts"

@Component
class CacheConfig {

    @Bean
    fun manager(): ConcurrentMapCacheManager {
        return ConcurrentMapCacheManager()
    }
}

@Component
class CacheInvalidator(
    val manager: ConcurrentMapCacheManager,
    val postCacheService: PostCacheService,
) {

    val cacheBlockingKeys: ConcurrentHashMap<String, ConcurrentHashMap<String, ReentrantLock>> = ConcurrentHashMap()

    @JmsListener(destination = CACHE_UPDATE_QUEUE)
    fun invalidate(cacheKeys: PostCacheChunk) {
        val cacheLocks = cacheBlockingKeys.getOrPut(POST_UPDATE_CACHE) {
            ConcurrentHashMap()
        }
        val authorLock = cacheLocks.getOrPut(cacheKeys.author) {
            ReentrantLock()
        }
        try {
            authorLock.lock()
            logger.info("Successfully locked $authorLock, will invalidate $cacheKeys")
            val elapsed = measureTime  {
                val cache = manager.getCache(POST_UPDATE_CACHE)
                cacheKeys.subscribers.forEach {
                    cache?.evictIfPresent(it)
                    // принудительно обновляем кэш
                    postCacheService.getLastBatch(it)
                }
            }
            logger.info("Invalidated $cacheKeys in ${elapsed.inWholeSeconds} second(s)")
        } finally {
            logger.info("Successfully unlocked $authorLock")
            authorLock.unlock()
        }
    }
}

data class PostCacheChunk(
    @JsonProperty("author")
    val author: String,
    @JsonProperty("subscribers")
    val subscribers: List<String>
) {
    override fun toString(): String {
        return "PostCacheChunk(author='$author', ${subscribers.size} subscribers)"
    }
}