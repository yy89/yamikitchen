package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.message.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by Johnson on 2015/5/12.
 */
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query(value = "SELECT * FROM Notice WHERE id in ?1 ORDER BY FIELD(id, ?1)", nativeQuery = true)
    public List<Notice> findByIdIn(Collection<String> noticeIdList);
}
