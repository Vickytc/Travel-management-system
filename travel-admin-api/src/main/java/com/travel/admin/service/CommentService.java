package com.travel.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.admin.model.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface CommentService extends IService<Comment> {
    /**
     * Paginated conditional query for the comment list
     */
    Page<Comment> getPageByCondition(Page<Comment> page, Comment comment);

    /**
     * Add a comment
     */
    boolean addComment(Comment comment, Integer reserveId);

    /**
     * Delete comment information
     */
    boolean deleteCommentById(Integer id);

    /**
     * Update comment information
     */
    boolean updateComment(Comment comment);

    Page<Comment> getCommentAllByUserId(Page<Comment> page, Integer userId);
}

