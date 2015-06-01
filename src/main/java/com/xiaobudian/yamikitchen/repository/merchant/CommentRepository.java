package com.xiaobudian.yamikitchen.repository.merchant;

import com.xiaobudian.yamikitchen.domain.merchant.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findByMerchantIdOrderByPublishDateDesc(Long merchantId, Pageable pageable);

    @Query("select avg(c.star) from Comment c where c.merchantId = ?1")
    public Double avgOfMerchantStar(Long merchantId);
}
