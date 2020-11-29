import exceptions.ExpressionIsNotSimplifiedException
import exceptions.OperatorNotFoundException

/**
 * Calculates given expression
 * @throws ExpressionIsNotSimplifiedException thrown exception if [exceptionCallback] is null
 * @throws OperatorNotFoundException thrown exception if [exceptionCallback] is null
 */
open class Calculator(
    protected val calculationCallback: ((expr: String) -> Unit)? = null,
    protected val exceptionCallback: ((exception: Exception) -> Unit)? = null
) {

    /**
     * Calculates given expression.
     * Sends intermediate results to [calculationCallback].
     * Can throw exception if [exceptionCallback] is null
     * @param expr expression to calculate
     * @throws ExpressionIsNotSimplifiedException
     * @throws OperatorNotFoundException
     */
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