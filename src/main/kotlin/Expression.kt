import kotlin.math.exp

internal class Expression(private var expr: String) {

    init {
        expr = expr.replace(" ", "")
    }

    val isNotNumber: Boolean
        get() = expr.toDoubleOrNull() == null


    @Throws(ExpressionIsNotSimplifiedException::class, OperatorNotFoundException::class)
    fun simplifyExpression(): String {

        val bracketsRegex = Regex("""\([^\(^\)]+\)""")
        val bracketsMatchResult = bracketsRegex.find(expr)

        return if (bracketsMatchResult != null) {
            val value = bracketsMatchResult.value
            val firstIndex = bracketsMatchResult.range.first
            val lastIndex = bracketsMatchResult.range.last

            val operatorRegex = Regex("""[\+\-\*\/]""")

            if (operatorRegex.findAll(value).count() == 1){
                expr = StringBuilder(expr)
                    .replace(firstIndex, firstIndex + 1, "")
                    .replace(lastIndex-1, lastIndex, "")
                    .toString()
            }

            simplifyExpressionWithoutBrackets(value)
        } else {
            simplifyExpressionWithoutBrackets(expr)
        }
    }

    @Throws(ExpressionIsNotSimplifiedException::class, OperatorNotFoundException::class)
    private fun simplifyExpressionWithoutBrackets(exprWithoutBrackets: String): String {
        Operators.forEach { operator ->
            val operatorRegex = Regex("""(\d+\.\d+|\d+)$operator(\d+\.\d+|\d+)""")
            operatorRegex.find(exprWithoutBrackets)?.let { matchResult ->
                val value = matchResult.value
                val pureOperator = operator.drop(1)
                val (firstNumber, secondNumber) = value.split(pureOperator).map { it.toDouble() }
                expr = expr.replaceFirst(value, Operators.calculate(operator, firstNumber, secondNumber).toString())
                return expr
            }
        }
        throw ExpressionIsNotSimplifiedException()
    }

    override fun toString(): String {
        return expr
    }
}