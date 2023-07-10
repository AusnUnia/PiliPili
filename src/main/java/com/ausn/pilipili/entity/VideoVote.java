package com.ausn.pilipili.entity;

import lombok.Data;

/*
the relationship between user's downvote or upvote and video
 */

/*
create table video_votes
(
 bv varchar(11),
 userId varchar(11),
 downvote boolean,
 upvote boolean,
 primary key (bv,userId)
)
*/
@Data
public class VideoVote
{
    private String bv;
    private String userId;
    private Boolean downvote;
    private Boolean upvote;
}
