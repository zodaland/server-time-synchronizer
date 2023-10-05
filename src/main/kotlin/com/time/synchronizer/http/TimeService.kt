package com.time.synchronizer.http

import com.time.synchronizer.domain.TimeAverage
import com.time.synchronizer.domain.TimeAverageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.net.MalformedURLException
import java.net.URL

@Service
class TimeService @Autowired constructor (
    val timeAverageRepository: TimeAverageRepository
) {
    fun saveOrActivate(domain: String): Boolean {
        try {
            val host = URL(domain).host
            timeAverageRepository.saveOrActivate(host)
            return true;
        } catch (e: MalformedURLException) {
            println("exception")
            return false;
        }
    }

    fun findAverage(domain: Any?): Flux<String> {
        if (domain == null) {
            return Flux.just("null");
        }
        val timeAverage = timeAverageRepository.findByDomain(domain as String)
        return Flux.just(timeAverage.getAverageTermMillis().toString());
    }
}