package com.demo.advanced.controller;

import com.demo.advanced.controller.ratelimit.SearchBuckets;
import com.demo.advanced.dto.response.BucketCapacityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
@Endpoint(id = "ratelimit")
@RequiredArgsConstructor
public class RateLimitCapacityResource {

    private final SearchBuckets searchBuckets;

    @ReadOperation
    public List<BucketCapacityResponse> customEndpoint() {
        return searchBuckets.getAllBucketsAvailableTokens();
    }

}
