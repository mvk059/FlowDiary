package fyi.manpreet.flowdiary

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import fyi.manpreet.flowdiary.usecase.MainActivityUseCase
import fyi.manpreet.flowdiary.util.Constants
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainActivity : ComponentActivity(), KoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        get<MainActivityUseCase>().setActivity(this)

        // Remove the title bar
        window.requestFeature(Window.FEATURE_NO_TITLE)
        enableEdgeToEdge()
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = true

        installSplashScreen()

        val widgetOpenRecord = intent.extras?.getBoolean(Constants.WIDGET_RECORD) ?: false
        setContent {
            App(
                widgetOpenRecord = widgetOpenRecord
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        get<MainActivityUseCase>().setActivity(null)
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}