package com.time.synchronizer.configuration

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@EnableCaching
@EnableScheduling
@Configuration
class CacheConfiguration {
    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = ConcurrentMapCacheManager();
        cacheManager.isAllowNullValues = false
        cacheManager.setCacheNames(listOf("timeAverages", "activeDomains"))

        return cacheManager;
    }
}