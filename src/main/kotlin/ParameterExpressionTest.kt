private fun main() {
    val parameterCalculator = ParameterCalculator(calculationCallback = { println(it) })

    parameterCalculator.calculate(
        "10*b+a+100*1000",
        listOf(
            mapOf(
                "a" to 10.0,
                "b" to 1.0,
            ),
            mapOf(
                "a" to 1.0,
                "b" to 1.0,
            )
        )
    )
}