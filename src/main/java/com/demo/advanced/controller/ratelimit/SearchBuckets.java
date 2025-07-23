package com.demo.advanced.controller.ratelimit;

import com.demo.advanced.dto.response.BucketCapacityResponse;
import io.github.bucket4j.Bucket;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SearchBuckets {

    private final RedisClient redisClient;
    private final Map<RateLimitType, RateLimitStrategy> strategies;

    public SearchBuckets(RedisClient redisClient, final List<RateLimitStrategy> strategiesBeans) {
        this.strategies = strategiesBeans.stream().collect(Collectors.toMap(RateLimitStrategy::getType, Function.identity()));
        this.redisClient = redisClient;
    }

    public List<BucketCapacityResponse> getAllBucketsAvailableTokens() {

        final Set<String> keys;

        try (StatefulRedisConnection<String, byte[]> redisConnection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE))) {

            final RedisCommands<String, byte[]> syncCommands = redisConnection.sync();

            KeyScanCursor<String> cursor = syncCommands.scan();
            keys = new HashSet<>(cursor.getKeys());

            while (!cursor.isFinished()) {
                cursor = syncCommands.scan(cursor);
                keys.addAll(cursor.getKeys());
            }

        }

        final List<BucketCapacityResponse> bucketsWithCapacity = new ArrayList<>(keys.size());

        keys.forEach(key -> {

            final RateLimitType type = RateLimitType.valueOf(key.split("-")[0]);

            final Bucket bucket = strategies.get(type).getOrCreateBucket(key);

            bucketsWithCapacity.add(new BucketCapacityResponse(key, bucket.getAvailableTokens()));

        });


        return bucketsWithCapacity;
    }

}
