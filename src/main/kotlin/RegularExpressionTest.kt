import utils.toExpression

private fun main(){
    val calculator = Calculator(calculationCallback = { println(it)})

    val matrixExpression1 = "10+30*(e*pi+10)".toExpression
    calculator.calculate(matrixExpression1)
}