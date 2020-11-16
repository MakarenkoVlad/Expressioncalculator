import utils.toExpression

private fun main() {
    val calculator = Calculator(calculationCallback = { println(it) })
    val parameterCalculator = ParameterCalculator(calculationCallback = { println(it) })
    println(calculator.calculate("(1.5 + sin(2.0 + cos(3.5)))*3".toExpression))
//    println(calculator.calculate("(2 +pi)*3+(10+30+((20)/10))".toExpression))
//    println(calculator.calculate("(10+pi)*3+(10+30+(tan(20)/10))".toExpression))
//    println(
//        parameterCalculator.calculate(
//            "(a+pi)*3+(10+30+(tan(b)/10))", listOf(
//                mapOf(
//                    "a" to 12.0,
//                    "b" to 12.0
//                ),
//                mapOf(
//                    "a" to 15.0,
//                    "b" to 15.0
//                )
//            )
//        )
//    )


}