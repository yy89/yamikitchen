package com.xiaobudian.yamikitchen.domain.message;

/**
 * Created by Johnson on 2015/5/27.
 */
public enum MessageType {
    COMMENT(0, "comment"), REPLY_COMMENT(1, "replyComment");
    private final int code;
    private final String name;

    MessageType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static MessageType fromCode(int code) {
        for (MessageType mt : values()) {
            if (code == mt.getCode()) return mt;
        }
        return null;
    }

    public boolean isComment() {
        return this.equals(COMMENT) || this.equals(REPLY_COMMENT);
    }

}
