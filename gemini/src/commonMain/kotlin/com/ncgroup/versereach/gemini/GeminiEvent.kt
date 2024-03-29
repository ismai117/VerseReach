package com.ncgroup.versereach.gemini


sealed interface GeminiEvent {
    class PROMPT(val value: String) : GeminiEvent
    data object SUBMIT : GeminiEvent
    object CLEAR_MESSAGE : GeminiEvent
}