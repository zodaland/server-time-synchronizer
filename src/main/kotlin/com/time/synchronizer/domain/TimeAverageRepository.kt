package com.time.synchronizer.domain

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Repository

@Repository
@CacheConfig(cacheNames = ["timeAverages", "activeDomains"])
class TimeAverageRepository {
    private val store: MutableMap<String, TimeAverage> = LinkedHashMap();
    private val activeDomainStore: MutableMap<String, TimeAverage> = LinkedHashMap();

    @CachePut(key = "#domain")
    @CacheEvict(key = "'all'")
    fun saveOrActivate(domain: String): TimeAverage {
        val timeAverage = store.getOrPut(domain) { TimeAverage() }
        activeDomainStore.getOrPut(domain) { timeAverage }
        timeAverage.lastModified = System.currentTimeMillis();

        return timeAverage;
    }

    @Cacheable(key = "#domain")
    fun findByDomain(domain: String): TimeAverage {
        if (!store.containsKey(domain)) {
            return saveOrActivate(domain);
        }
        return store[domain]!!
    }

    @Cacheable(key = "'all'")
    fun findAll(): Map<String, TimeAverage> {
        return store
    }

    @Cacheable(key = "'all'")
    fun findActiveAll(): Map<String, TimeAverage> {
        return activeDomainStore
    }

    @CacheEvict(cacheNames = ["activeDomains"])
    @Scheduled(fixedRate = 60000)
    fun deleteIdleDomains() {
        val idleDomainStore = activeDomainStore.filterValues { timeAverage -> timeAverage.lastModified < System.currentTimeMillis() - 600000 }
        for (domain: String in idleDomainStore.keys) {
            activeDomainStore.remove(domain)
        }
    }

    @Scheduled(fixedRate = 1000)
    fun printAll() {
        findAll().keys.forEach { println(it) }
    }
}