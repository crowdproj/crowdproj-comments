import kotlin.reflect.KClass

actual fun log(
    mes: String,
    clazz: KClass<*>,
    level: LogLevel,
) {
    println("${clazz.qualifiedName} ${level.name} $mes")
}