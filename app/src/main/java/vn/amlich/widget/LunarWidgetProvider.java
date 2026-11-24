package vn.amlich.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * Widget chỉ hiển thị: không gắn sự kiện bấm nên chạm vào không mở app.
 * Tự cập nhật lúc qua nửa đêm, khi đổi ngày/giờ/múi giờ và sau khi khởi động lại máy.
 */
public class LunarWidgetProvider extends AppWidgetProvider {

    static final String ACTION_MIDNIGHT = "vn.amlich.widget.ACTION_MIDNIGHT";
    private static final String[] DOW = {"CN", "T2", "T3", "T4", "T5", "T6", "T7"};

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {
        RemoteViews views = buildViews();
        for (int id : appWidgetIds) {
            manager.updateAppWidget(id, views);
        }
        scheduleNextMidnight(context);
    }

    @Override
    public void onEnabled(Context context) {
        scheduleNextMidnight(context);
    }

    @Override
    public void onDisabled(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.cancel(midnightIntent(context));
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        if (ACTION_MIDNIGHT.equals(action)
                || Intent.ACTION_DATE_CHANGED.equals(action)
                || Intent.ACTION_TIME_CHANGED.equals(action)
                || Intent.ACTION_TIMEZONE_CHANGED.equals(action)
                || Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            updateAll(context);
            scheduleNextMidnight(context);
        }
    }

    static void updateAll(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, LunarWidgetProvider.class));
        if (ids == null || ids.length == 0) {
            return;
        }
        RemoteViews views = buildViews();
        for (int id : ids) {
            manager.updateAppWidget(id, views);
        }
    }

    private static PendingIntent midnightIntent(Context context) {
        Intent i = new Intent(context, LunarWidgetProvider.class);
        i.setAction(ACTION_MIDNIGHT);
        return PendingIntent.getBroadcast(
                context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    static void scheduleNextMidnight(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 5);
        cal.set(Calendar.MILLISECOND, 0);
        am.set(AlarmManager.RTC, cal.getTimeInMillis(), midnightIntent(context));
    }

    static String solarText(Calendar now) {
        return DOW[now.get(Calendar.DAY_OF_WEEK) - 1] + ", "
                + now.get(Calendar.DAY_OF_MONTH) + "/"
                + (now.get(Calendar.MONTH) + 1) + "/"
                + now.get(Calendar.YEAR);
    }

    static RemoteViews buildViews() {
        Calendar now = Calendar.getInstance();
        int[] today = LunarCalendar.solar2lunar(
                now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR));
        Calendar tomorrow = (Calendar) now.clone();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        int[] next = LunarCalendar.solar2lunar(
                tomorrow.get(Calendar.DAY_OF_MONTH),
                tomorrow.get(Calendar.MONTH) + 1,
                tomorrow.get(Calendar.YEAR));

        RemoteViews v = new RemoteViews("vn.amlich.widget", R.layout.widget_lunar);
        v.setTextViewText(R.id.tv_solar, solarText(now));
        v.setTextViewText(R.id.tv_day, String.valueOf(today[0]));
        v.setTextViewText(
                R.id.tv_month, "tháng " + today[1] + (today[3] == 1 ? " nhuận" : "") + " âm lịch");
        v.setTextViewText(R.id.tv_year, "Năm " + LunarCalendar.canChi(today[2]));

        String label = LunarCalendar.label(today, next);
        if (label.isEmpty()) {
            v.setViewVisibility(R.id.tv_event, View.GONE);
        } else {
            v.setTextViewText(R.id.tv_event, label);
            v.setViewVisibility(R.id.tv_event, View.VISIBLE);
        }
        return v;
    }
}
