package com.time.synchronizer.configuration

import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

class DomainHandshakeWebSocketService: HandshakeWebSocketService() {
    override fun handleRequest(exchange: ServerWebExchange, handler: WebSocketHandler): Mono<Void> {
        val domain = exchange.request.queryParams["domain"]?.get(0)
        if (domain.isNullOrBlank()) {
            return Mono.error<Void>(ServerWebInputException("domain not found"));
        }
        println(domain)
        return
        exchange.session.attributes["domain"] = domain

        return super.handleRequest(exchange, handler)
    }
}