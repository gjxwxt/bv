package dev.aaa1115910.bv.mobile.activities

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.aaa1115910.bv.mobile.screen.MobileMainScreen
import dev.aaa1115910.bv.mobile.theme.BVMobileTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
        val isTelevision = uiModeManager?.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION ||
                packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
        if (isTelevision) {
            runCatching {
                startActivity(Intent(this, Class.forName("dev.aaa1115910.bv.tv.activities.MainActivity")))
                finish()
                return
            }
        }

        var keepSplashScreen = true
        installSplashScreen().apply {
            setKeepOnScreenCondition { keepSplashScreen }
        }

        setContent {
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                scope.launch(Dispatchers.IO) {
                    keepSplashScreen = false
                }
            }

            BVMobileTheme {
                MobileMainScreen()
            }
        }
    }
}