package com.crowdproj.comments.mappers.v1.exceptions

import com.crowdproj.comments.common.models.CommentCommand

class UnknownCommandException(command: CommentCommand): RuntimeException("Wrong command $command at mapping toTransport stage")