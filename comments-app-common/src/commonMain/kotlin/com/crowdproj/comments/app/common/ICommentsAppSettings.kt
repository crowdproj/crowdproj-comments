package com.crowdproj.comments.app.common

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.config.CommentsCorSettings

interface ICommentsAppSettings {
    val processor: CommentProcessor
    val corSettings: CommentsCorSettings
}