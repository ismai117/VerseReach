package org.ncgroup.mymail.validation.email

import org.ncgroup.mymail.validation.ValidationResult

class ValidateContent {
    operator fun invoke(content: String): ValidationResult {
        if (content.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Email Content is required."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}