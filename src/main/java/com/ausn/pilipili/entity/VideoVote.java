package com.ausn.pilipili.entity;

import lombok.Data;

/*
the relationship between user's downvote or upvote and video
 */

/*
create table video_votes
(
 bv varchar(11),
 userId Bigint,
 downvote boolean,
 upvote boolean,
 primary key (bv,userId)
)
*/
@Data
public class VideoVote
{
    private String bv;
    private Long userId;
    private int vote; // 1 is upvote, 0 is no action, -1 is downvote
}
