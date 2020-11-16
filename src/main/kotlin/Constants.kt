/**
 * Object that converts string constants to double constants
 */
object Constants {

    private val constantsMap = mapOf(
        "pi" to 3.14,
        "e" to 2.718
    )

    operator fun get(constant: String): Double? = constantsMap[constant]
}