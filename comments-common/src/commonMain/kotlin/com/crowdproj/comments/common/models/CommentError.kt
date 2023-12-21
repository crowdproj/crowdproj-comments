package com.crowdproj.comments.common.models

data class CommentError(
    val code: String = "",
    val group: Group = Group.NONE,
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: Level = Level.ERROR,
) {
    @Suppress("unused")
    enum class Level {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    @Suppress("unused")
    enum class Group (val alias: String){
        NONE(""),
        SERVER("internal-server"),
        VALIDATION("validation"),
        REQUEST("request"),
        REPOSITORY("repository"),
        ADMINISTRATION("administration"),
    }
}