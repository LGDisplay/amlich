package vn.amlich.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

/** Màn hình hướng dẫn rất ngắn, đồng thời hiện ngày âm hôm nay để kiểm tra. */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar now = Calendar.getInstance();
        int[] l = LunarCalendar.solar2lunar(
                now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR));

        String text = "Hôm nay: " + LunarWidgetProvider.solarText(now) + "\n"
                + "Âm lịch: " + l[0] + "/" + l[1] + (l[3] == 1 ? " nhuận" : "")
                + " năm " + LunarCalendar.canChi(l[2]) + "\n\n"
                + "Cách thêm widget:\n"
                + "1. Nhấn giữ vào chỗ trống trên màn hình chính\n"
                + "2. Chọn Widget (Tiện ích)\n"
                + "3. Tìm \"Âm lịch Việt Nam\" và kéo ra màn hình\n\n"
                + "Widget chỉ hiển thị, chạm vào không mở app. Tính offline, không cần mạng.";

        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16);
        int pad = (int) (24 * getResources().getDisplayMetrics().density);
        tv.setPadding(pad, pad * 2, pad, pad);
        setContentView(tv);
    }
}
