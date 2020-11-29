import utils.toExpression

fun main() {
    firstTest()
}

fun firstTest() {
    val calculator = Calculator(calculationCallback = { println(it) })

    val result = calculator.calculate("!(0 \\/ ((1))) /\\ !((1))".toExpression)

    assert(result == "0")
}