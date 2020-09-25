class Calculator private constructor(
    private val calculationCallback: ((expr: String) -> Unit)?,
    private val exceptionCallback: ((exception: Exception) -> Unit)?
) {

    companion object {
        fun build(setFieldsAndLambdas: Builder.() -> Unit): Calculator {
            val builder = Builder()

            builder.setFieldsAndLambdas()

            return builder.build()
        }
    }

    fun calculate(expr: String): String {

        val calculatedExpression = Expression(expr)

        return try {
            while (calculatedExpression.isNotNumber) {
                calculatedExpression.simplifyExpression()
                calculationCallback?.invoke(calculatedExpression.toString())
            }

            calculatedExpression.toString()
        } catch (e: Exception) {

            if (exceptionCallback == null) {
                throw when (e) {
                    is ExpressionIsNotSimplifiedException -> e
                    is OperatorNotFoundException -> e
                    else -> e
                }
            } else {
                exceptionCallback.invoke(e)
                ""
            }
        }
    }

    class Builder {

        private var calculationCallback: ((expr: String) -> Unit)? = null
        private var exceptionCallback: ((exception: Exception) -> Unit)? = null

        fun setCalculationCallback(callback: (expr: String) -> Unit): Builder {
            calculationCallback = callback
            return this
        }

        fun setExceptionCallback(callback: (exception: Exception) -> Unit): Builder {
            exceptionCallback = callback
            return this
        }

        fun build(): Calculator = Calculator(calculationCallback, exceptionCallback)
    }
}