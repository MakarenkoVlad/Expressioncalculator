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