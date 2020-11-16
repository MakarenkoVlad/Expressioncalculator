import utils.isOperandOrDouble
import utils.operandToDouble
import utils.replaceFirst

internal class RegularExpression(private var expr: String) : Expression {
    private val operatorRegex = Regexes.getOperators()

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
    override fun simplifyExpression(): String {
        val bracketsRegex = Regexes.bracket
        val bracketsWithUnaryRegex = Regexes.bracketWithUnary
        val bracketsMatchResult = bracketsRegex.find(expr)

        val bracketsWithUnaryMatchResult = bracketsWithUnaryRegex.find(expr)
        if (bracketsWithUnaryMatchResult != null) {
            val matchResult = bracketsRegex.find(bracketsWithUnaryMatchResult.value)!!
            var (value, firstIndex, lastIndex) = decomposeMatchResult(matchResult)
            val startingFrom = bracketsWithUnaryMatchResult.range.first
            // If operators count = 1 -> remove brackets
            when (operatorRegex.findAll(value).count()) {
                1 -> {
                    expr = expr
                        .removeRange(startingFrom + lastIndex, startingFrom + lastIndex + 1)
                        .removeRange(startingFrom, startingFrom + firstIndex + 1)
                    if (value[1] == '-')
                        value = value.replace("[()]".toRegex(), "")
                }
                0 -> {
                    expr = expr
                        .removeRange(startingFrom + lastIndex, startingFrom + lastIndex + 1)
                        .removeRange(startingFrom, startingFrom + firstIndex + 1)

                    value = value.replace("[()]".toRegex(), "")
                }
            }
            val operator =
                bracketsWithUnaryMatchResult.value.replace("""(\)|\(|\d|\.)""".toRegex(), "")
            return simplifyExpressionWithoutBrackets(startingFrom, value) { Operators.calculate(operator, it) }
        } else if (bracketsMatchResult != null) {

            var (value, firstIndex, lastIndex) = decomposeMatchResult(bracketsMatchResult)

            // If operators count < 1 -> remove brackets
            when (operatorRegex.findAll(value).count()) {
                2 -> {
                    val minusRegex = Regex("""(^|\+|\-|\*|\/)-(\d+\.\d+|\d+|\w\w|\w)""")
                    if (minusRegex.find(value) != null) {
                        expr = StringBuilder(expr)
                            .replace(firstIndex, firstIndex + 1, "")
                            .replace(lastIndex - 1, lastIndex, "")
                            .toString()

                        value = value.replace("[()]".toRegex(), "")
                    }
                }
                1 -> {
                    expr = StringBuilder(expr)
                        .replace(firstIndex, firstIndex + 1, "")
                        .replace(lastIndex - 1, lastIndex, "")
                        .toString()
                    if (value[1] == '-')
                        value = value.replace("[()]".toRegex(), "")
                }
                0 -> {
                    expr = StringBuilder(expr)
                        .replace(firstIndex, firstIndex + 1, "")
                        .replace(lastIndex - 1, lastIndex, "")
                        .toString()

                    value = value.replace("[()]".toRegex(), "")
                }
            }
//            println(expr)

            return simplifyExpressionWithoutBrackets(firstIndex, value)
        } else {
            return simplifyExpressionWithoutBrackets(0, expr)
        }
    }

    /**
     * @return first is value, second is first index, third is last index
     */
    private fun decomposeMatchResult(matchResult: MatchResult): Triple<String, Int, Int> {
        return Triple(matchResult.value, matchResult.range.first, matchResult.range.last)
    }

    /**
     * Simplifies given expression without brackets and replaces that expression in class expression
     * @throws ExpressionIsNotSimplifiedException when expression cannot be simplified
     * @throws OperatorNotFoundException when in expression no operators
     * @param exprWithoutBrackets expression that do not contain brackets
     * @return simplified part of expression
     */
    private fun simplifyExpressionWithoutBrackets(
        position: Int,
        exprWithoutBrackets: String,
        onPostCalculate: ((Double) -> Double)? = null
    ): String {
        Operators.forEachIndexed { index, operator ->
            val operatorRegex = Regexes.getOperator(operator)
            operatorRegex.find(exprWithoutBrackets)?.let { matchResult ->
                val value = matchResult.value
                simplifyBinaryExpression(position, value, index, operator, onPostCalculate)
                return expr
            }
        }

        if (exprWithoutBrackets.isOperandOrDouble) {
            return simplifyUnaryExpression(position, exprWithoutBrackets, onPostCalculate)
        } else {
            throw ExpressionIsNotSimplifiedException()
        }
    }

    private fun simplifyUnaryExpression(
        position: Int,
        exprWithoutBrackets: String,
        onPostCalculate: ((Double) -> Double)?
    ): String {
        val value = (onPostCalculate?.invoke(exprWithoutBrackets.operandToDouble())
            ?: exprWithoutBrackets.toDouble()).toString()
        return expr.replaceFirst(exprWithoutBrackets, value, position).also {
            expr = it
        }
    }

    /**
     * Simplifies expression with two operands
     */
    private fun simplifyBinaryExpression(
        position: Int,
        expression: String,
        operatorIndexInOperators: Int,
        operator: String,
        onPostCalculate: ((Double) -> Double)? = null
    ) {
        val (firstNumber, secondNumber) = expression.split(Operators.pureBinaryOperators[operatorIndexInOperators])
            .map { it.operandToDouble() }
        val temporaryResult = Operators.calculate(operator, firstNumber, secondNumber)
        expr = expr.replaceFirst(
            expression,
            (onPostCalculate?.invoke(temporaryResult) ?: temporaryResult).toString(),
            position
        )
    }

    override fun toString(): String {
        return expr
    }
}