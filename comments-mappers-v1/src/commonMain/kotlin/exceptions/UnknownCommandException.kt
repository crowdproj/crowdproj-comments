package exceptions

import com.crowdproj.comments.common.models.CommentCommand

class UnknownCommandException(command: CommentCommand): RuntimeException("Wrong command $command at mapping toTransport stage")