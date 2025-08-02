package com.beforeyoubet.errors

data class ApiFailedException(val error: ErrorMessage) : Exception()