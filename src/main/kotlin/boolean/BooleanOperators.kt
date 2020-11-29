package boolean

import exceptions.OperatorNotFoundException

internal object BooleanOperators {
    val binaryOperators = listOf(
        "/\\",
        "\\/",
    )

    private val binaryOperatorsFunc = mapOf(
        """/\""" to {first: Int, second: Int -> if (first == 0 || second == 0) 0 else 1},
        """\/""" to {first: Int, second: Int -> if (first == 1 || second == 1) 1 else 0}
    )

    val unaryOperators = listOf(
        "!"
    )

    fun calculateBinary(operator: String, first: Int, second: Int): Int {
        return binaryOperatorsFunc[operator]?.invoke(first, second) ?: throw OperatorNotFoundException()
    }
}