package com.demo.advanced.dto.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
public final class RateLimitEvent extends ApplicationEvent {

    private final String key;

    public RateLimitEvent(Object source, String key) {
        super(source);
        this.key = key;
    }

}
