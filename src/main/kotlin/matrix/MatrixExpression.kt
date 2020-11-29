package matrix

import BaseExpression
import exceptions.ExpressionIsNotSimplifiedException
import utils.*
import kotlin.math.exp

internal class MatrixExpression(override var expr: String) : BaseExpression(expr) {
    private val operatorRegex = MatrixRegexes.getOperators()

    override fun simplifyExpression(): String {
        val bracketsRegex = MatrixRegexes.bracket
        val bracketsWithUnaryRegex = MatrixRegexes.bracketWithUnary
        val bracketsMatchResult = bracketsRegex.find(expr)

        val bracketsWithUnaryMatchResult = bracketsWithUnaryRegex.find(expr)
        if (bracketsWithUnaryMatchResult != null) {
            val matchResult = bracketsRegex.find(bracketsWithUnaryMatchResult.value)!!
            var (value, firstIndex, lastIndex) = matchResult.decomposeMatchResult()
            val startingFrom = bracketsWithUnaryMatchResult.range.first
            // If operators count = 1 -> remove brackets
            when (operatorRegex.findAll(value).count()) {
                2 -> {
                    val minusRegex = MatrixRegexes.minusRegex
                    if (minusRegex.find(value) != null) {
                        expr = expr
                            .removeRange(startingFrom + lastIndex, startingFrom + lastIndex + 1)
                            .removeRange(startingFrom, startingFrom + firstIndex + 1)

                        value = value.replace("[()]".toRegex(), "")
                    }
                }
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
                bracketsWithUnaryMatchResult.value.replace("""(\W|\d)""".toRegex(), "")
            return simplifyExpressionWithoutBrackets(startingFrom, value) { MatrixOperators.calculate(operator, it) }
        } else if (bracketsMatchResult != null) {

            var (value, firstIndex, lastIndex) = bracketsMatchResult.decomposeMatchResult()

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
     * Simplifies given expression without brackets and replaces that expression in class expression
     * @throws ExpressionIsNotSimplifiedException when expression cannot be simplified
     * @throws OperatorNotFoundException when in expression no operators
     * @param exprWithoutBrackets expression that do not contain brackets
     * @return simplified part of expression
     */
    private fun simplifyExpressionWithoutBrackets(
        position: Int,
        exprWithoutBrackets: String,
        onPostCalculate: ((Array<DoubleArray>) -> Array<DoubleArray>)? = null
    ): String {
        MatrixOperators.forEachIndexed { index, operator ->
            val operatorRegex = MatrixRegexes.getOperator(operator)
            operatorRegex.find(exprWithoutBrackets)?.let { matchResult ->
                val value = matchResult.value
                simplifyBinaryExpression(position, value, index, operator, onPostCalculate)
                return expr
            }
        }

//        if (exprWithoutBrackets.isMa) {
//            return simplifyUnaryExpression(position, exprWithoutBrackets, onPostCalculate)
//        } else {
        throw ExpressionIsNotSimplifiedException()
//        }
    }

    private fun simplifyUnaryExpression(
        position: Int,
        exprWithoutBrackets: String,
        onPostCalculate: ((Array<DoubleArray>) -> Array<DoubleArray>)?
    ): String {
        val value = (onPostCalculate?.invoke(exprWithoutBrackets.toMatrix())
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
        onPostCalculate: ((Array<DoubleArray>) -> Array<DoubleArray>)? = null
    ) {
        val (firstNumber, secondNumber) = expression.split(MatrixOperators.pureBinaryOperators[operatorIndexInOperators])
            .map { stringMatrix -> stringMatrix.toMatrix() }
        val temporaryResult = MatrixOperators.calculate(operator, firstNumber, secondNumber)
        expr = expr.replaceFirst(
            expression,
            (onPostCalculate?.invoke(temporaryResult)?.toMatrixString() ?: temporaryResult.toMatrixString()).toString(),
            position
        )
    }

    override val isExpression: Boolean
        get() {
            val leftBrackets = expr.count { it == '[' }
            val rightBrackets = expr.count { it == ']' }
            return leftBrackets == rightBrackets && leftBrackets > 1
        }
}