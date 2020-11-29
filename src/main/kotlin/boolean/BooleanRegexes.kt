package boolean

object BooleanRegexes {
    val bracket = Regex("""\([^\(^\)]+\)""")
    val bracketWithUnary = Regex("""!\([^\(^\)]+\)""")
    val binaryOperator = Regex("""(\\\/|\/\\)""")

    fun getBinaryOperator(operator: String): Regex = Regex("""[10]$operator[10]""")
}