package com.time.synchronizer.domain

data class TimeAverage(
        private var reqCount: Int,
        private var totalTermMillis: Long,
        var lastModified: Long
) {
    constructor(): this(0, 0, 0)

    fun addTermMillis(termMillis: Long) {
        reqCount++
        totalTermMillis += termMillis;
    }

    fun getAverageTermMillis(): Long {
        return if (reqCount > 0) totalTermMillis / reqCount else 0
    }
}
