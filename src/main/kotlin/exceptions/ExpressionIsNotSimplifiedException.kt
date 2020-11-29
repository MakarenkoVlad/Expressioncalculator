package exceptions

class ExpressionIsNotSimplifiedException: Exception {
    constructor() : super("Expression is not simplified")
    constructor(message: String): super(message)
}