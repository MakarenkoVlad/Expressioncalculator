class ParameterCalculator(calculationCallback: ((String) -> Unit)? = null,
                          exceptionCallback: ((Exception) -> Unit)? = null
) {
    private val calculator = Calculator(calculationCallback, exceptionCallback)

    fun calculate(expr: String, parameters: List<Map<String, Double>>): List<String> {
        val results = mutableListOf<String>()
        parameters.forEach {map ->
            var expression = expr
            map.forEach { (key, item) ->
                val parameterRegex = Regexes.getParameter(key)
                expression = expression.replace(parameterRegex) { matchResult ->
                    val value = matchResult.value
                    val index = value.indexOf(key)
                    value.replaceRange(index, index+1, item.toString())
                }
            }
            results.add(calculator.calculate(expression))
        }
        return results
    }
}