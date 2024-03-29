package org.ncgroup.versereach.email.presentation

import org.ncgroup.versereach.utils.ResultState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import email.service.Recipient
import org.ncgroup.versereach.email.domain.repository.EmailRepository
import org.ncgroup.versereach.utils.validation.email.ValidateContent
import org.ncgroup.versereach.utils.validation.email.ValidateRecipientEmail
import org.ncgroup.versereach.utils.validation.email.ValidateSubject


class EmailScreenModel(
    private val emailRepository: EmailRepository
) : ScreenModel {

    private val validateRecipientEmail = ValidateRecipientEmail()
    private val validateSubject = ValidateSubject()
    private val validateContent = ValidateContent()

    var state by mutableStateOf(EmailState())

    val recipients = mutableStateListOf<String>()

    fun onEvent(event: EmailEvent) {
        when (event) {
            is EmailEvent.RECIPIENT -> {
                state = state.copy(recipient = event.recipient)
            }

            is EmailEvent.SUBJECT -> {
                state = state.copy(subject = event.subject)
            }

            is EmailEvent.CONTENT -> {
                state = state.copy(content = event.content)
            }

            is EmailEvent.SUBMIT -> {
                sendEmail()
            }

            is EmailEvent.CLEAR_ERROR_MESSAGES -> {
                clearErrorMessages()
            }

            is EmailEvent.RESET_UI_STATE -> {
                resetUIState()
            }
        }
    }

    private fun sendEmail() {

        val recipientsResult = validateRecipientEmail(email = recipients.toSet().toList())
        val subjectResult = validateSubject(subject = state.subject)
        val contentResult = validateContent(content = state.content)

        val hasError = listOf(
            recipientsResult,
            subjectResult,
            contentResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                recipientsError = recipientsResult.errorMessage,
                subjectError = subjectResult.errorMessage,
                contentError = contentResult.errorMessage
            )
            return
        }

        screenModelScope.launch {
            emailRepository.sendEmail(
                recipients = recipients.map { Recipient(email = it) },
                subject = state.subject,
                content = state.content
            ).collect { result ->
                state = state.copy(isLoading = false, status = false, error = "")
                when (result) {
                    is ResultState.Loading -> {
                        state = state.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        state = state.copy(
                            isLoading = false,
                            status = true
                        )
                    }

                    is ResultState.Error -> {
                        state = state.copy(
                            isLoading = false,
                            status = false,
                            error = state.error
                        )
                    }
                }
            }
        }
    }

    fun addRecipient(recipient: String) {
        if (!recipients.contains(recipient)) {
            recipients.add(recipient)
        }
    }

    private fun resetUIState() {
        state = state.copy(
            status = false,
            recipient = "",
            subject = "",
            content = ""
        )
        recipients.clear()
    }

    private fun clearErrorMessages() {
        state = state.copy(
            recipientsError = "",
            subjectError = "",
            contentError = ""
        )
    }

}
