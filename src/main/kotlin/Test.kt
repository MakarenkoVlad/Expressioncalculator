private fun main() {
    val calculator = Calculator.build {
        setCalculationCallback {
            println(it)
        }
    }

//    println(calculator.calculate("(2 +pi)*3+(10+30+((20)/10))"))
    println(calculator.calculate("(2 +pi)*3+(10+30+(tan(20)/10))"))
    println()
    println(calculator.calculate("(2+2)*3+(10+*30+(20/10))"))
}