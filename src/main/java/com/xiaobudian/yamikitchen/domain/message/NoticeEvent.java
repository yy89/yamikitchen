package com.xiaobudian.yamikitchen.domain.message;

import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * Created by Johnson on 2015/5/12.
 */
public class NoticeEvent extends ApplicationEvent {
    private static final long serialVersionUID = -8369799416540055334L;
    private List<Notice> notices;

    public NoticeEvent(Object source, List<Notice> notices) {
        super(source);
        this.notices = notices;
    }

    public List<Notice> getNotices() {
        return notices;
    }
}
