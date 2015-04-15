package com.github.bandwidthlimiter.bucket

import com.github.bandwidthlimiter.Limiters
import spock.lang.Specification


class BandwidthAlgorithmsSpecification extends Specification {

    def "GetAvailableTokens specification"(long requiredAvailableTokens, Bucket bucket) {
        setup:
            Bandwidth[] bandwidths = bucket.configuration.bandwidths
            BucketState state = bucket.createSnapshot()
        when:
            long availableTokens = BandwidthAlgorithms.getAvailableTokens(bandwidths, state)
        then:
            availableTokens == requiredAvailableTokens
        where:
            requiredAvailableTokens |                    bucket
                    10              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100).buildLocalUnsafe()
                     0              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 0).buildLocalUnsafe()
                     5              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).buildLocalUnsafe()
                     2              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).withLimitedBandwidth(2, 10).buildLocalUnsafe()
                     3              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).withLimitedBandwidth(2, 10).withGuaranteedBandwidth(3, 1000).buildLocalUnsafe()
                     2              | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).withLimitedBandwidth(2, 10).withGuaranteedBandwidth(1, 1000).buildLocalUnsafe()
    }

    def "calculateTimeToCloseDeficit specification"(long toConsume, long requiredTime, Bucket bucket) {
        setup:
            Bandwidth[] bandwidths = bucket.configuration.bandwidths
            BucketState state = bucket.createSnapshot()
        when:
            long actualTime = BandwidthAlgorithms.delayAfterWillBePossibleToConsume(bandwidths, state, 0, toConsume)
        then:
            actualTime == requiredTime
        where:
            toConsume | requiredTime |                               bucket
               10     |    100         | Limiters.withCustomTimePrecision(new TimeMeterMock(0)).withLimitedBandwidth(10, 100, 0).buildLocalUnsafe()
//                4   |     40       | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 6).buildLocalUnsafe()
//                 0       | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 0).buildLocalUnsafe()
//                 5       | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).buildLocalUnsafe()
//                 2       | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).withLimitedBandwidth(2, 10).buildLocalUnsafe()
//                 3       | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).withLimitedBandwidth(2, 10).withGuaranteedBandwidth(3, 1000).buildLocalUnsafe()
//                 2       | Limiters.withNanoTimePrecision().withLimitedBandwidth(10, 100, 5).withLimitedBandwidth(2, 10).withGuaranteedBandwidth(1, 1000).buildLocalUnsafe()

    }

}