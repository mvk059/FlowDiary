package fyi.manpreet.flowdiary.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview


@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "phone", device = "spec:width=360dp,height=640dp,dpi=480")
@Preview(name = "landscape", device = "spec:width=640dp,height=360dp,dpi=480")
@Preview(name = "foldable", device = "spec:width=673dp,height=841dp,dpi=480")
@Preview(name = "tablet", device = "spec:width=1280dp,height=800dp,dpi=480")
@Preview(
    name = "Dark Mode 1.5x",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    fontScale = 1.5f,
)
@Preview(
    name = "Light Mode 1.5x",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    fontScale = 1.5f,
)
@Preview(
    name = "Dark Mode 2x",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    fontScale = 2f,
)
@Preview(
    name = "Light Mode 2x",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    fontScale = 2f,
)
@Preview(device = Devices.PIXEL_4)
@Preview(device = "spec:width=360dp,height=640dp,dpi=480")
annotation class DevicePreview