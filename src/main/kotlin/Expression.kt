/**
 * @throws OperatorNotFoundException
 * @throws ExpressionIsNotSimplifiedException
 */
interface Expression {

    /**
     * Simplifies the expression by calculating the most priority operation
     */
    fun simplifyExpression(): String

    /**
     * Tells you is that expression is number
     */
    val isExpression: Boolean
}