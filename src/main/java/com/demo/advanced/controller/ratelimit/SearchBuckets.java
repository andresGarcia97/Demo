package com.demo.advanced.controller.ratelimit;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchBuckets {

    private final RedisClient redisClient;

    public List<String> getAllBucketKeys() {

        final List<String> keys = new ArrayList<>();

        try (StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE))) {

            final RedisCommands<String, byte[]> syncCommands = connection.sync();

            KeyScanCursor<String> cursor = syncCommands.scan();
            keys.addAll(cursor.getKeys());

            while (!cursor.isFinished()) {
                cursor = syncCommands.scan(cursor);
                keys.addAll(cursor.getKeys());
            }

        }

        return keys;
    }

}
