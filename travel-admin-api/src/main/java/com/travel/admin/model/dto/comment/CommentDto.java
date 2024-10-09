package com.travel.admin.model.dto.comment;

import com.travel.admin.model.DtoConvertDo;
import com.travel.admin.model.domain.Comment;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class CommentDto implements DtoConvertDo<Comment> {
    @NotNull(message = "Comment ID cannot be empty when modifying", groups = UpdateGroup.class)
    private Integer id;

    /**
     * Comment content
     */
    @NotBlank(message="Comment content cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String content;

    /**
     * Service rating
     */
    @NotNull(message="Service rating cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer serviceRating;

    /**
     * Hotel ID
     */
    @NotNull(message="Hotel ID for the comment cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer hotelId;

    /**
     * Room ID
     */
    @NotNull(message="Room ID for the comment cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer roomId;

    /**
     * Commenter ID
     */
    @NotNull(message="Commenter ID cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer userId;

    /**
     * Comment status: 0 is unreviewed, 1 is approved
     */
    @NotNull(message="Comment status cannot be empty when modifying", groups = {UpdateGroup.class})
    private Integer state;

    @Override
    public Comment convert() {
        return Comment.builder()
                .id(id)
                .content(content)
                .serviceRating(serviceRating)
                .hotelId(hotelId)
                .roomId(roomId)
                .userId(userId)
                .state(state)
                .build();
    }
}

