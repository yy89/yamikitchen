package com.xiaobudian.yamikitchen.domain.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.message.Notice;
import com.xiaobudian.yamikitchen.domain.message.NoticeTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Johnson on 2015/5/10.
 */
public enum OrderStatus {
    WAIT_PAYMENT(1), WAIT_CONFIRMATION(2), WAIT_DELIVER(3), WAIT_SELF_TAKE(4), DELIVERING(5), COMPLETED(6), CANCELLED(7);

    public static final Collection<Integer> IN_PROGRESS = Arrays.asList(WAIT_PAYMENT.getIndex(), WAIT_CONFIRMATION.getIndex(), WAIT_DELIVER.getIndex(), DELIVERING.getIndex());
    public static final Collection<Integer> SOLVED = Arrays.asList(DELIVERING.getIndex(), COMPLETED.getIndex());
    public static final Collection<Integer> PROCESSING = Arrays.asList(WAIT_CONFIRMATION.getIndex(), WAIT_DELIVER.getIndex());
    public static final Collection<Integer> PENDING = Arrays.asList(WAIT_CONFIRMATION.getIndex(), WAIT_DELIVER.getIndex(), WAIT_SELF_TAKE.getIndex(), COMPLETED.getIndex());


    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private static List<NoticeTemplate> TEMPLATES;

    static {
        try {
            TEMPLATES = MAPPER.readValue(new ClassPathResource("noticeConfig.json").getFile(), new TypeReference<List<NoticeTemplate>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final int index;

    OrderStatus(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static OrderStatus from(int index) {
        for (OrderStatus status : values()) {
            if (status.getIndex() == index) return status;
        }
        return null;
    }

    public NoticeTemplate getTemplate() {
        for (NoticeTemplate t : TEMPLATES) {
            if (t.getOrderStatus() == index) return t;
        }
        return null;
    }

    public List<Notice> getNotices(Merchant merchant, Order order) {
        NoticeTemplate template = getTemplate();
        if (template == null) return null;
        List<Notice> notices = new ArrayList<>();
        final String content = PARSER.parseExpression(template.getContentTemplate(), new TemplateParserContext()).getValue(getEvaluationContext(merchant, order), String.class);
        if (this.equals(OrderStatus.WAIT_CONFIRMATION))
            notices.add(new Notice(3, template.getTitle(), content, order.getOrderNo(), merchant.getCreator()));
        if (this.equals(OrderStatus.WAIT_DELIVER))
            notices.add(new Notice(1, template.getTitle(), content, order.getOrderNo(), order.getUid()));
        if (this.equals(OrderStatus.CANCELLED)) {
            notices.add(new Notice(1, template.getTitle(), content, order.getOrderNo(), order.getUid()));
            notices.add(new Notice(1, template.getTitle(), content, order.getOrderNo(), merchant.getCreator()));
        }
        return notices;
    }

    private EvaluationContext getEvaluationContext(Merchant merchant, Order order) {
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("order", order);
        ctx.setVariable("merchant", merchant);
        return ctx;
    }
}
