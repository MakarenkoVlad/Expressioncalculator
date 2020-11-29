import utils.toExpression

private fun main() {
    val calculator = Calculator(calculationCallback = { println(it)})

    val matrixExpression1 = "[ 10 12 13 ]*[ 12 ; 13 ; 13]".toExpression
    val matrixExpression2 = "[ 10 12 13 ]+[ 12  13  13]".toExpression
    calculator.calculate(matrixExpression1)
    calculator.calculate(matrixExpression2)
}