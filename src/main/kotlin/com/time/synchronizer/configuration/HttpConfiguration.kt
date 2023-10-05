package com.time.synchronizer.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient
import java.time.Duration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Configuration
class HttpConfiguration {
    @Bean
    fun executor(): ExecutorService = Executors.newCachedThreadPool()

    @Bean
    @Throws(Exception::class)
    fun httpClient(executor: ExecutorService): HttpClient {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(1000))
                .version(HttpClient.Version.HTTP_1_1)
                .executor(executor)
                .build()
    }
}
