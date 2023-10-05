package com.time.synchronizer.http

import com.time.synchronizer.domain.TimeAverageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.MalformedURLException
import java.net.URL
import java.time.Duration

@Component
class TimeHandler @Autowired constructor(
        val timeService: TimeService
): WebSocketHandler {
    private final val PING_INTERNAL: Duration = Duration.ofSeconds(30)
    private final val TIME_INTERNAL: Duration = Duration.ofSeconds(1)
    private final val PING_PAYLOAD: ByteArray = byteArrayOf(0)

    override fun handle(session: WebSocketSession): Mono<Void> {
        val input = session.receive()
                .filter { it.type == WebSocketMessage.Type.TEXT }
                .map { it.payloadAsText }
                .filter { timeService.saveOrActivate(it) }
                .doOnNext { session.send(Mono.just(session.textMessage("asd"))) }
                .doOnNext { session.attributes["domain"] = it }
                .then()

        val domain = session.attributes["domain"]
        println(domain);
        var timeSource = timeService.findAverage(domain)
                .map(session::textMessage)

        val pingSource: Flux<WebSocketMessage> = Flux.interval(PING_INTERNAL)
                .map { session.pingMessage { it.wrap(PING_PAYLOAD) } }

        val output2 = session.send(Flux.merge(timeSource, pingSource)).then()

        return Mono.zip(input, output2).then()
    }
}