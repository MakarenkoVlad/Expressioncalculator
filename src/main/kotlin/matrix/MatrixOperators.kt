package matrix

import exceptions.ExpressionIsNotSimplifiedException
import exceptions.OperatorNotFoundException

internal object MatrixOperators : Iterable<String> {
    private val binaryOperators = listOf("""\*""", """\+""", """\-""")
    val pureBinaryOperators = listOf("""*""", """+""", """-""")
    private val binaryOperatorFuncs = mapOf(
        binaryOperators[0] to { a: Array<DoubleArray>, b: Array<DoubleArray> ->
            if (b.count() != a[0].count()) throw ExpressionIsNotSimplifiedException("Matrixes cannot be multiplied!")
            val resultColumns = b[0].count()
            val resultRows = a.count()
            val resultMatrix = Array(resultRows) { DoubleArray(resultColumns) }
            for (i in 0 until resultRows) {
                for (j in 0 until resultColumns) {
                    for (k in 0 until a[0].count()) resultMatrix[i][j] += a[i][k] * b[k][j]
                }
            }
            resultMatrix
        },
        binaryOperators[1] to { a: Array<DoubleArray>, b: Array<DoubleArray> ->
            if (a.count() != b.count() && a[0].count() != b[0].count()) throw ExpressionIsNotSimplifiedException("Matrixes cannot be summed")

            val resultMatrix = Array(a.count()) {DoubleArray(a[0].count())}

            for (i in 0 until a.count()) {
                for (j in 0 until a[0].count())
                    resultMatrix[i][j] = a[i][j] + b[i][j]
            }
            resultMatrix
        },
        binaryOperators[2] to { a: Array<DoubleArray>, b: Array<DoubleArray> ->
            if (a.count() != b.count() && a[0].count() != b[0].count()) throw ExpressionIsNotSimplifiedException("Matrixes cannot be minused")

            val resultMatrix = Array(a.count()) {DoubleArray(a[0].count())}

            for (i in 0 until a.count()) {
                for (j in 0 until a[0].count())
                    resultMatrix[i][j] = a[i][j] - b[i][j]
            }
            resultMatrix
        }
    )
    private val unaryOperatorFuncs = mapOf(
        "inv" to { a: Array<DoubleArray> ->
            val resultMatrix = a.clone()
            for (i in a.indices){
                for (j in a[0].indices)
                    resultMatrix[i][j] = a[j][i]
            }
            resultMatrix
        },
        "-" to { a: Array<DoubleArray> ->
            for (i in 0 until a.count()){
                for (j in 0 until a[0].count())
                    a[i][j] = -a[i][j]
            }
            a
        },
    )

    override fun iterator(): Iterator<String> = binaryOperators.iterator()

    /**
     * Calculates binary expression
     * @throws OperatorNotFoundException - if operator not found
     */
    fun calculate(operator: String, a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
        return binaryOperatorFuncs[operator]?.invoke(a, b) ?: throw OperatorNotFoundException()
    }

    /**
     * Calculates unary expression
     * @throws OperatorNotFoundException - if operator not found
     */
    fun calculate(operator: String, a: Array<DoubleArray>): Array<DoubleArray> {
        return unaryOperatorFuncs[operator]?.invoke(a) ?: throw OperatorNotFoundException()
    }
}