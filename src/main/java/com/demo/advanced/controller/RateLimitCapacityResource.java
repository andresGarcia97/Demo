package com.demo.advanced.controller;

import com.demo.advanced.controller.ratelimit.SearchBuckets;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Endpoint(id = "ratelimit")
@RequiredArgsConstructor
public class RateLimitCapacityResource {

    private final SearchBuckets searchBuckets;

    @ReadOperation
    public List<String> customEndpoint() {
        return searchBuckets.getAllBucketKeys();
    }

}
