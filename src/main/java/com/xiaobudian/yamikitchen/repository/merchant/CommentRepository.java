package com.xiaobudian.yamikitchen.repository.merchant;

import com.xiaobudian.yamikitchen.domain.merchant.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
