object Regexes {
    /**
    * Regex which could find bracket with biggest priority
    */
    val bracket = Regex("""\([^\(^\)]+\)""")
    val bracketWithUnary = Regex("""\w+\([^\(^\)]+\)""")

    /**
     * Regex must be updated if will be found constant longer than 2
     */
    fun getOperator(operator: String): Regex {
        return Regex("""(\d+\.\d+|\d+|\w\w|\w)$operator(\d+\.\d+|\d+|\w\w|\w)""")
    }

    fun getOperators() = Regex("""[+\-*/]""")
}