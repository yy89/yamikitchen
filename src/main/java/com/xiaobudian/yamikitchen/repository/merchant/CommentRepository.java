package com.xiaobudian.yamikitchen.repository.merchant;

import com.xiaobudian.yamikitchen.domain.merchant.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findByMerchantIdOrderByPublishDateDesc(Long merchantId, Pageable pageable);
}
