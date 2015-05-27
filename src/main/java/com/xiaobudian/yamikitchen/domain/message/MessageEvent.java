package com.xiaobudian.yamikitchen.domain.message;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Johnson on 2015/5/27.
 */
public class MessageEvent extends ApplicationEvent {
    private Message message;

    public MessageEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
