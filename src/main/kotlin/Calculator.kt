import exceptions.ExpressionIsNotSimplifiedException
import exceptions.OperatorNotFoundException

open class Calculator(
    protected val calculationCallback: ((expr: String) -> Unit)? = null,
    protected val exceptionCallback: ((exception: Exception) -> Unit)? = null
) {

    fun calculate(expr: Expression): String {
        return try {
            while (expr.isExpression) {
                expr.simplifyExpression()
                calculationCallback?.invoke(expr.toString())
            }

            expr.toString()
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