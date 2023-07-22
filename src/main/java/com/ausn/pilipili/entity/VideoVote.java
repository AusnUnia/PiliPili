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
    private Boolean downvote;
    private Boolean upvote;
}
