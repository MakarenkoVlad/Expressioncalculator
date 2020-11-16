package boolean

import BaseExpression

class BooleanExpression(override var expr: String): BaseExpression(expr) {
    override fun simplifyExpression(): String {
        return ""
    }


}