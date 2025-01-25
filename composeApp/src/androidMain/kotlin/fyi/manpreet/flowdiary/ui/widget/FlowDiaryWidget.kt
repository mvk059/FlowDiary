package fyi.manpreet.flowdiary.ui.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import fyi.manpreet.flowdiary.MainActivity
import fyi.manpreet.flowdiary.R
import fyi.manpreet.flowdiary.util.Constants

class FlowDiaryWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(androidx.glance.ImageProvider(R.drawable.ic_widget))
                    .clickable(
                        onClick = actionStartActivity<MainActivity>(
                            parameters = actionParametersOf(destinationKey to true)
                        )
                    )
            ) { }
        }
    }

    companion object {
        val SHOW_RECORD = ActionParameters.Key<Boolean>("show_record")
        val destinationKey = ActionParameters.Key<Boolean>(Constants.WIDGET_RECORD)
    }
}