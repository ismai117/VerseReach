package org.ncgroup.mymail.navigation


import androidx.compose.runtime.Composable
import org.ncgroup.mymail.email.presentation.EmailScreen
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.ncgroup.mymail.sms.presentation.SmsScreen


private const val EMAIL = "email_screen"
private const val SMS = "sms_screen"

@Composable
fun RootNavigation(
    navigator: Navigator
){
    NavHost(
        navigator = navigator,
        initialRoute = EMAIL,
        navTransition = NavTransition()
    ){
        scene(
            route = EMAIL,
            navTransition = NavTransition()
        ){
            EmailScreen(
                navigator = navigator
            )
        }
        scene(
            route = SMS,
            navTransition = NavTransition()
        ){
            SmsScreen(
                navigator = navigator
            )
        }
    }
}