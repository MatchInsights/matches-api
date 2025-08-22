package match.insights.errors

data class ApiFailedException(val error: ErrorMessage) : Exception()