package boolean

import BaseExpression
import exceptions.ExpressionIsNotSimplifiedException
import exceptions.OperatorNotFoundException
import utils.decomposeMatchResult

class BooleanExpression(override var expr: String) : BaseExpression(expr) {
    init {
        expr = expr.replace(" ", "")
    }

    private val binaryOperatorRegex = BooleanRegexes.binaryOperator
    private val bracketRegex = BooleanRegexes.bracket
    private val bracketWithUnaryRegex = BooleanRegexes.bracketWithUnary

    override fun simplifyExpression(): String {

        val unaryMatch = bracketWithUnaryRegex.find(expr)
        val match = bracketRegex.find(expr)

        return when {
            unaryMatch != null -> {
                val startingFrom = unaryMatch.range.first
                val matchResult = bracketRegex.find(unaryMatch.value)!!
                var (value, firstIndex, lastIndex) = matchResult.decomposeMatchResult()
                when (binaryOperatorRegex.findAll(value).count()) {
                    0, 1 -> {
                        expr = expr.removeRange(startingFrom + firstIndex - 1, startingFrom + firstIndex + 1)
                            .removeRange(startingFrom + lastIndex - 2, startingFrom + lastIndex - 1)
                        value = value.replace("""[()!]""".toRegex(), "")
                    }
                }

                simplifyExpressionWithoutBrackets(value) {
                    if (it == 1) 0 else 1
                }
            }
            match != null -> {
                var (value, firstIndex, lastIndex) = match.decomposeMatchResult()
                when (binaryOperatorRegex.findAll(value).count()) {
                    0, 1 -> {
                        expr = expr.replaceRange(firstIndex, firstIndex + 1, "")
                            .replaceRange(lastIndex - 1, lastIndex, "")
                        value = value.replace("""[()]""".toRegex(), "")
                    }
                }

                simplifyExpressionWithoutBrackets(value, null)
            }
            else -> {
                simplifyExpressionWithoutBrackets(expr, null)
            }
        }
    }

    private fun simplifyExpressionWithoutBrackets(value: String, onPostCalculate: ((result: Int) -> Int)?): String {
        BooleanOperators.binaryOperators.forEach { operator ->
            val index = value.indexOf(operator)
            if (index != -1) {
                val result = BooleanOperators.calculateBinary(
                    operator,
                    value[index - 1].toString().toInt(),
                    value[index + 2].toString().toInt())
                val finalResult = onPostCalculate?.invoke(result) ?: result
                expr = expr.replace(value, finalResult.toString())
                return expr
            }

        }
        if (value.toDoubleOrNull() == null)
            throw ExpressionIsNotSimplifiedException()
        else {
            val result = onPostCalculate?.invoke(value.toInt()) ?: value.toInt()
            expr = expr.replace(value, result.toString())
            return expr
        }
    }

    override fun toString(): String {
        return expr
    }
}