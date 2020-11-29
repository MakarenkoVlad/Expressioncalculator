import exceptions.ExpressionIsNotSimplifiedException
import exceptions.OperatorNotFoundException
import regular.RegularExpression
import regular.RegularRegexes

/**
 * Allows calculate parametric expression with 'a' and 'b' parameter
 * @throws ExpressionIsNotSimplifiedException thrown exception if [exceptionCallback] is null
 * @throws OperatorNotFoundException thrown exception if [exceptionCallback] is null
 */
class ParameterCalculator(
    calculationCallback: ((String) -> Unit)? = null,
    exceptionCallback: ((Exception) -> Unit)? = null
) {
    private val calculator = Calculator(calculationCallback, exceptionCallback)

    /**
     * Replaces a, b in expressions with [parameters] and calculates that expressions
     * @throws ExpressionIsNotSimplifiedException thrown exception if [exceptionCallback] is null
     * @throws OperatorNotFoundException thrown exception if [exceptionCallback] is null
     */
    fun calculate(expr: String, parameters: List<Map<String, Double>>): List<String> {
        val results = mutableListOf<String>()
        parameters.forEach { map ->
            var expression = expr
            map.forEach { (key, item) ->
                val parameterRegex = RegularRegexes.getParameter(key)
                expression = expression.replace(parameterRegex) { matchResult ->
                    val value = matchResult.value
                    val index = value.indexOf(key)
                    value.replaceRange(index, index + 1, item.toString())
                }
            }
            results.add(calculator.calculate(RegularExpression(expression)))
        }
        return results
    }
}