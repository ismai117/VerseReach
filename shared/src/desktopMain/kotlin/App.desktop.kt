package org.ncgroup.versereach

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable


actual fun getPlatformName(): String {
    return ""
}

@Composable
fun MainView() = App()

@Preview
@Composable
fun AppPreview() {
    App()
}

actual fun openUrl(url: String?) {
}