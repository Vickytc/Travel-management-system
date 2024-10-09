package com.travel.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.admin.model.Result;
import com.travel.admin.model.domain.Comment;
import com.travel.admin.model.domain.Reserve;
import com.travel.admin.model.dto.comment.CommentDto;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import com.travel.admin.service.CommentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
     * Paginated conditional query for the comment list
     */
    @PostMapping("/page/{pageNum}/{pageSize}")
    public Result getPageByCondition(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                     @RequestBody Comment comment) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        Page<Comment> commentPage = commentService.getPageByCondition(page, comment);
        return Result.success(commentPage);
    }

    /**
     * Add a comment
     */
    @PostMapping
    public Result addComment(@Validated(AddGroup.class) @RequestBody CommentDto dto,
                             @RequestParam("reserveId") Integer reserveId) {
        Comment comment = dto.convert();
        return commentService.addComment(comment, reserveId) ? Result.success("Comment added successfully!") : Result.error("Failed to add comment!");
    }

    /**
     * Delete comment information
     */
    @DeleteMapping("/{id}")
    public Result deleteComment(@Valid @PathVariable @NotNull(message="Delete comment ID cannot be null") Integer id) {
        return commentService.deleteCommentById(id) ? Result.success("Comment deleted successfully!") : Result.error("Failed to delete comment!");
    }

    /**
     * Modify comment information
     */
    @PutMapping
    public Result updateComment(@Validated(UpdateGroup.class) @RequestBody CommentDto dto) {
        Comment comment = dto.convert();
        return commentService.updateComment(comment) ? Result.success("Comment updated successfully!") : Result.error("Failed to update comment!");
    }

    /**
     * View all reviews for your hotel
     */
    @GetMapping("/page/all/{userId}/{pageNum}/{pageSize}")
    public Result getCommentAllByUserId(@PathVariable("userId") Integer userId, @PathVariable Integer pageNum,
                                        @PathVariable Integer pageSize) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        Page<Comment> commentPage = commentService.getCommentAllByUserId(page, userId);
        return Result.success(commentPage);
    }
}
