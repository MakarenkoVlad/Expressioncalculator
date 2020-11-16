import utils.operandToDouble

internal class RegularExpression(private var expr: String): Expression {

    init {
        expr = expr.replace(" ", "")
    }

    override val isNotNumber: Boolean
        get() = expr.toDoubleOrNull() == null


    /**
     * Takes high priority expression and removes brackets if there 1 operator
     * and then passes it to function which process high priority binary operator
     * @throws ExpressionIsNotSimplifiedException when expression cannot be simplified
     * @throws OperatorNotFoundException when in expression no operators
     * @return simplified part of expression
     */
    @Throws(ExpressionIsNotSimplifiedException::class, OperatorNotFoundException::class)
    override fun simplifyExpression(): String {
        val bracketsRegex = Regexes.bracket
        val bracketsMatchResult = bracketsRegex.find(expr)

        return if (bracketsMatchResult != null) {
            val value = bracketsMatchResult.value
            val firstIndex = bracketsMatchResult.range.first
            val lastIndex = bracketsMatchResult.range.last

            val operatorRegex = Regexes.getOperators()

            // If operators count = 1 -> remove brackets
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

    /**
     * Simplifies expression without brackets
     * @throws ExpressionIsNotSimplifiedException when expression cannot be simplified
     * @throws OperatorNotFoundException when in expression no operators
     * @param exprWithoutBrackets expression that do not contain brackets
     * @return simplified part of expression
     */
    @Throws(ExpressionIsNotSimplifiedException::class, OperatorNotFoundException::class)
    private fun simplifyExpressionWithoutBrackets(exprWithoutBrackets: String): String {
        Operators.forEachIndexed { index, operator ->
            val operatorRegex = Regexes.getOperator(operator)
            operatorRegex.find(exprWithoutBrackets)?.let { matchResult ->
                val value = matchResult.value
                simplifyBinaryExpression(value, index, operator)
                return expr
            }
        }
        throw ExpressionIsNotSimplifiedException()
    }

    /**
     * Simplifies expression with two operands
     */
    private fun simplifyBinaryExpression(
        expression: String,
        operatorIndexInOperators: Int,
        operator: String
    ) {
        val (firstNumber, secondNumber) = expression.split(Operators.pureOperators[operatorIndexInOperators])
            .map { it.operandToDouble() }
        expr = expr.replaceFirst(expression, Operators.calculate(operator, firstNumber, secondNumber).toString())
    }

    override fun toString(): String {
        return expr
    }
}