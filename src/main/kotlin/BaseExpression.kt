abstract class BaseExpression(protected open var expr: String): Expression {
    abstract override fun simplifyExpression(): String

    override val isExpression: Boolean
        get() = expr.toDoubleOrNull() == null

    override fun toString(): String {
        return expr
    }
}