package matrix

object MatrixRegexes {
    val minusRegex = Regex("""(^|\+|\-|\*|\/)-\[[^\[^\]]+\]""")

    /**
     * Regex which could find bracket with biggest priority
     */
    val bracket = Regex("""\([^\(^\)]+\)""")
    val bracketWithUnary = Regex("""\w+\([^\(^\)]+\)""")

    /**
     * Regex must be updated if will be found constant longer than 2
     */
    fun getOperator(operator: String): Regex {
        return Regex("""\[[^\[^\]]+\]$operator(-.+|\[[^\[^\]]+\])""")
    }

    fun getOperators() = Regex("""[+\-*/]""")

    fun getParameter(parameter: String) = Regex("""(\W|^)$parameter(\W|${'$'})""")
}