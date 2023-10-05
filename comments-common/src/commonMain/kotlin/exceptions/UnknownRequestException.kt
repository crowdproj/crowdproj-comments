package com.crowdproj.comments.common.exceptions

import kotlin.reflect.KClass

class UnknownRequestException(cls: KClass<*>): RuntimeException("Class $cls can't be mapped and not supported")