package com.crowdproj.comments.app.common

import com.crowdproj.comments.biz.CommentProcessor

interface ICommentsAppSettings {
    val processor: CommentProcessor
}