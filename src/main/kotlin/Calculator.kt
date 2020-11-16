open class Calculator(
    protected val calculationCallback: ((expr: String) -> Unit)? = null,
    protected val exceptionCallback: ((exception: Exception) -> Unit)? = null
) {

    fun calculate(expr: String): String {
        val calculatedExpression = RegularExpression(expr) as Expression

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
}