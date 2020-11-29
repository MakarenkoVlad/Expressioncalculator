package utils

import exceptions.ConstantNotFoundException
import Constants
import Expression
import boolean.BooleanExpression
import matrix.MatrixExpression
import regular.RegularExpression

/**
 * Utility function that turn operand to double even if it constant, else throws exception
 * @throws ConstantNotFoundException - thrown if operand is not double and constant not found
 */
fun String.operandToDouble(): Double {
    val result = toDoubleOrNull()
    return result ?: Constants[this] ?: throw ConstantNotFoundException()
}

fun String.toMatrix(): Array<DoubleArray> {
    return replace(Regex("""[\[\]]"""), "")
        .split(";").map { doubles ->
            doubles.split(" ").mapNotNull { it.toDoubleOrNull() }.toDoubleArray()
        }.toTypedArray()
}

val String.isOperandOrDouble: Boolean
    get() {
        val result = toDoubleOrNull()
        return result ?: Constants[this] != null
    }

fun String.replaceFirst(oldValue: String, newValue: String, startingFrom: Int): String {
    val secondPartOfString = substring(startingFrom)
    val firstPartOfString = substring(0, startingFrom)
    return firstPartOfString + secondPartOfString.replaceFirst(oldValue, newValue)
}

val String.toExpression: Expression
    get() {
        return when {
            this.contains("""[\[\]]""".toRegex()) -> MatrixExpression(this)
            this.contains("""[\\/!]""".toRegex()) -> BooleanExpression(this)
            else -> RegularExpression(this)
        }
    }