package com.time.synchronizer.configuration

import com.time.synchronizer.domain.TimeAverageRepository
import com.time.synchronizer.http.TimeHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebSocketConfiguration {
    @Bean
    fun handlerMapping(timeHandler: TimeHandler): HandlerMapping {
        val map = mapOf("/ws" to timeHandler)
        val order = -1

        return SimpleUrlHandlerMapping(map, order)
    }

    @Bean
    fun handlerAdapter() = WebSocketHandlerAdapter(webSocketService())

    @Bean
    fun webSocketService() = DomainHandshakeWebSocketService()
}