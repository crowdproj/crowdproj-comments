import kotlin.reflect.KClass

expect fun log(
    mes: String,
    clazz: KClass<*>,
    level: LogLevel = LogLevel.INFO,
)

enum class LogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
}
