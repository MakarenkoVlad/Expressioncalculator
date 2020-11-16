internal object Operators : Iterable<String> {
    private val operators = listOf("""\*""", """\/""", """\+""", """\-""")
    val pureOperators = listOf("""*""", """/""", """+""", """-""")
    private val operatorFuncs = mapOf(
        operators[0] to {a: Double, b: Double -> a * b},
        operators[1] to {a: Double, b: Double -> a / b},
        operators[2] to {a: Double, b: Double -> a + b},
        operators[3] to {a: Double, b: Double -> a - b}
    )

    override fun iterator(): Iterator<String> = operators.iterator()

    @Throws(OperatorNotFoundException::class)
    fun calculate(operator: String, firstNum: Double, secondNum: Double): Double {
        val operatorFun = operatorFuncs[operator]
        if (operatorFun == null)
            throw OperatorNotFoundException()
        else{
            return operatorFun(firstNum, secondNum)
        }
    }
}