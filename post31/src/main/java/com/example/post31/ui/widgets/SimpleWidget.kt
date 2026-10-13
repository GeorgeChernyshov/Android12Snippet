package com.example.post31.ui.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.util.SizeF
import android.widget.RemoteViews
import com.example.post31.R
import com.example.post31.ui.widgets.SimpleWidget.Companion.TITLE_PREFS_KEY

/**
 * Implementation of App Widget functionality.
 */
class SimpleWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        const val TITLE_PREFS_KEY = "SimpleWidgetTitle"
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context
        .getSharedPreferences(WIDGETS_PREFS, Context.MODE_PRIVATE)
        .getString(
            TITLE_PREFS_KEY + appWidgetId,
            context.getString(R.string.appwidget_text)
        )

    // Construct the RemoteViews object
    val views = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val compactView = RemoteViews(
            context.packageName,
            R.layout.simple_widget
        ).apply {
            setTextViewText(R.id.appwidget_text, widgetText)
        }

        val expandedView = RemoteViews(
            context.packageName,
            R.layout.simple_widget_expanded
        ).apply {
            setTextViewText(R.id.appwidget_text, widgetText)
        }

        val viewMapping = mapOf(
            SizeF(40f, 40f) to compactView,
            SizeF(150f, 250f) to expandedView,
            SizeF(300f, 150f) to expandedView
        )

        RemoteViews(viewMapping)
    } else RemoteViews(context.packageName, R.layout.simple_widget).apply {
        setTextViewText(R.id.appwidget_text, widgetText)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}