package com.xiaobudian.yamikitchen.repository.message;

import com.xiaobudian.yamikitchen.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by Johnson on 2015/5/27.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "FROM Message m WHERE m.id in (?1) ORDER BY FIELD(m.id, ?1)")
    public List<Message> findByIdIn(Collection<Long> msgIdList);
}
