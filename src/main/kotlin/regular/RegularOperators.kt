package regular

import exceptions.OperatorNotFoundException

internal object RegularOperators : Iterable<String> {
    private val binaryOperators = listOf("""\*""", """\/""", """\+""", """\-""")
    val pureBinaryOperators = listOf("""*""", """/""", """+""", """-""")
    private val binaryOperatorFuncs = mapOf(
        binaryOperators[0] to { a: Double, b: Double -> a * b },
        binaryOperators[1] to { a: Double, b: Double -> a / b },
        binaryOperators[2] to { a: Double, b: Double -> a + b },
        binaryOperators[3] to { a: Double, b: Double -> a - b }
    )
    private val unaryOperatorFuncs = mapOf(
        "sin" to Math::sin,
        "cos" to Math::cos,
        "tan" to Math::tan
    )

    override fun iterator(): Iterator<String> = binaryOperators.iterator()

    /**
     * Calculates binary expression
     * @throws OperatorNotFoundException - if operator not found
     */
    fun calculate(operator: String, firstNum: Double, secondNum: Double): Double {
        return binaryOperatorFuncs[operator]?.invoke(firstNum, secondNum) ?: throw OperatorNotFoundException()
    }

    /**
     * Calculates unary expression
     * @throws OperatorNotFoundException - if operator not found
     */
    fun calculate(operator: String, number: Double): Double {
        return unaryOperatorFuncs[operator]?.invoke(number) ?: throw OperatorNotFoundException()
    }
}