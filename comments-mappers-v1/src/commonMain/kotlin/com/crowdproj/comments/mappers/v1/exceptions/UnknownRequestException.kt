package com.crowdproj.comments.mappers.v1.exceptions

import kotlin.reflect.KClass

class UnknownRequestException(cls: KClass<*>): RuntimeException("Class $cls can't be mapped and not supported")