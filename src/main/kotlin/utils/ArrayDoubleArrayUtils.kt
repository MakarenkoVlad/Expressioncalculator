package utils

fun Array<DoubleArray>.toMatrixString(): String {
    val builder = StringBuilder("[")
    builder.append(firstOrNull()?.rowToString()?:"")
    for (i in 1 until count()) builder.append("; ${getOrNull(i)?.rowToString()?:""}")
    builder.append("]")
    return builder.toString()
}

fun DoubleArray.rowToString(): String {
    val builder = StringBuilder()
    builder.append("${firstOrNull()?:""} ")
    for (i in 1 until count()) builder.append(" ${getOrNull(i)?:""} ")
    return builder.toString()
}

fun Array<DoubleArray>.print(): Array<DoubleArray> {
    forEach { doubles -> doubles.forEach { print("$it, ") }; println(";") }
    return this
}