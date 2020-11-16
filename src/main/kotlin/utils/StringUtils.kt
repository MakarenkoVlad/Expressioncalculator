package utils

import ConstantNotFoundException
import Constants

/**
 * Utility function that turn operand to double even if it constant, else throws exception
 * @throws ConstantNotFoundException - thrown if operand is not double and constant not found
 */
fun String.operandToDouble(): Double {
    val result = toDoubleOrNull()
    return result ?: Constants[this] ?: throw ConstantNotFoundException()
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