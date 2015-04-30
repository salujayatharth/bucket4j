package com.github.bandwidthlimiter.bucket

import com.github.bandwidthlimiter.Limiters
import com.github.bandwidthlimiter.bucket.mock.TimeMeterMock
import spock.lang.Specification

import static java.lang.Long.MAX_VALUE


class BucketStateSpecification extends Specification {

    def "GetAvailableTokens specification"(long requiredAvailableTokens, Bucket bucket) {
        setup:
            Bandwidth[] bandwidths = bucket.configuration.bandwidths
            BucketState state = bucket.createSnapshot()
        when:
            long availableTokens = state.getAvailableTokens(bandwidths)
        then:
            availableTokens == requiredAvailableTokens
        where:
            requiredAvailableTokens |                    bucket
                    10              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100).buildLocalNonSynchronized()
                     0              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 0).buildLocalNonSynchronized()
                     5              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).buildLocalNonSynchronized()
                     2              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).withLimitedBandwidth(2, 10).buildLocalNonSynchronized()
                     3              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).withLimitedBandwidth(2, 10).withGuaranteedBandwidth(3, 1000).buildLocalNonSynchronized()
                     2              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).withLimitedBandwidth(2, 10).withGuaranteedBandwidth(1, 1000).buildLocalNonSynchronized()
    }

    def "delayAfterWillBePossibleToConsume specification"(long toConsume, long requiredTime, Bucket bucket) {
        setup:
            Bandwidth[] bandwidths = bucket.configuration.bandwidths
            BucketState state = bucket.createSnapshot()
        when:
            long actualTime = state.delayAfterWillBePossibleToConsume(bandwidths, 0, toConsume)
        then:
            actualTime == requiredTime
        where:
            toConsume | requiredTime |                               bucket
               10     |    100       | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(10, 100, 0).buildLocalNonSynchronized()
                7     |     30       | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(10, 100, 4).buildLocalNonSynchronized()
               11     |  MAX_VALUE   | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(10, 100, 4).buildLocalNonSynchronized()
                3     |     20       | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(10, 100, 1).withLimitedBandwidth(5, 10, 2).buildLocalNonSynchronized()
                3     |     20       | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(5, 10, 2).withLimitedBandwidth(10, 100, 1).buildLocalNonSynchronized()
                3     |      0       | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(5, 10, 2).withGuaranteedBandwidth(10, 100, 9).buildLocalNonSynchronized()
                6     |      0       | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(5, 10, 2).withGuaranteedBandwidth(10, 100, 9).buildLocalNonSynchronized()
                4     |      4       | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(5, 10, 0).withGuaranteedBandwidth(25, 100, 3).buildLocalNonSynchronized()
                4     |      2       | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(5, 10, 3).withGuaranteedBandwidth(25, 100, 3).buildLocalNonSynchronized()
    }

}