package org.example.myclock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.util.Calendar;
import java.util.Locale;

public class ClockWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new ClockEngine();
    }

    private class ClockEngine extends Engine {

        private final Paint timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint datePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private boolean visible = false;

        private final Runnable drawRunnable = new Runnable() {
            @Override
            public void run() {
                drawClock();

                if (visible) {
                    getSurfaceHolder()
                            .getSurface()
                            .isValid();

                    new android.os.Handler(
                            android.os.Looper.getMainLooper()
                    ).postDelayed(this, 1000);
                }
            }
        };

        ClockEngine() {

            timePaint.setColor(Color.WHITE);
            timePaint.setTypeface(
                    Typeface.create(
                            Typeface.DEFAULT,
                            Typeface.BOLD
                    )
            );
            timePaint.setTextAlign(Paint.Align.CENTER);

            datePaint.setColor(Color.WHITE);
            datePaint.setTypeface(
                    Typeface.create(
                            Typeface.DEFAULT,
                            Typeface.NORMAL
                    )
            );
            datePaint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {

            this.visible = visible;

            if (visible) {
                drawClock();
                drawRunnable.run();
            }
        }

        @Override
        public void onSurfaceChanged(
                SurfaceHolder holder,
                int format,
                int width,
                int height
        ) {
            super.onSurfaceChanged(
                    holder,
                    format,
                    width,
                    height
            );

            drawClock();
        }

        @Override
        public void onSurfaceDestroyed(
                SurfaceHolder holder
        ) {
            visible = false;
            super.onSurfaceDestroyed(holder);
        }

        private void drawClock() {

            SurfaceHolder holder = getSurfaceHolder();

            Canvas canvas = null;

            try {

                canvas = holder.lockCanvas();

                if (canvas == null) {
                    return;
                }

                int width = canvas.getWidth();
                int height = canvas.getHeight();

                canvas.drawColor(Color.BLACK);

                float centerX = width / 2f;

                float scale =
                        Math.min(width, height)
                                / 1080f;

                // =========================
                // ساعت
                // =========================

                timePaint.setTextSize(
                        190f * scale
                );

                Calendar now =
                        Calendar.getInstance();

                int hour =
                        now.get(Calendar.HOUR);

                if (hour == 0) {
                    hour = 12;
                }

                int minute =
                        now.get(Calendar.MINUTE);

                String hourText =
                        String.format(
                                Locale.US,
                                "%02d",
                                hour
                        );

                String minuteText =
                        String.format(
                                Locale.US,
                                "%02d",
                                minute
                        );

                float centerY =
                        height * 0.40f;

                float spacing =
                        145f * scale;

                Paint.FontMetrics fm =
                        timePaint.getFontMetrics();

                float offset =
                        -(fm.ascent + fm.descent) / 2f;

                canvas.drawText(
                        hourText,
                        centerX,
                        centerY
                                - spacing / 2f
                                + offset,
                        timePaint
                );

                canvas.drawText(
                        minuteText,
                        centerX,
                        centerY
                                + spacing / 2f
                                + offset,
                        timePaint
                );

                // =========================
                // تاریخ‌ها
                // =========================

                datePaint.setTextSize(
                        50f * scale
                );

                int gy =
                        now.get(Calendar.YEAR);

                int gm =
                        now.get(Calendar.MONTH) + 1;

                int gd =
                        now.get(
                                Calendar.DAY_OF_MONTH
                        );

                // شمسی
                int[] jalali =
                        gregorianToJalali(
                                gy,
                                gm,
                                gd
                        );

                String jalaliText =
                        String.format(
                                Locale.US,
                                "%04d/%02d/%02d",
                                jalali[0],
                                jalali[1],
                                jalali[2]
                        );

                // میلادی
                String gregorianText =
                        String.format(
                                Locale.US,
                                "%04d/%02d/%02d",
                                gy,
                                gm,
                                gd
                        );

                // قمری
                int[] hijri =
                        gregorianToHijri(
                                gy,
                                gm,
                                gd
                        );

                String hijriText =
                        String.format(
                                Locale.US,
                                "%04d/%02d/%02d",
                                hijri[0],
                                hijri[1],
                                hijri[2]
                        );

                float startY =
                        height * 0.72f;

                float dateSpacing =
                        60f * scale;

                canvas.drawText(
                        jalaliText,
                        centerX,
                        startY,
                        datePaint
                );

                canvas.drawText(
                        gregorianText,
                        centerX,
                        startY
                                + dateSpacing,
                        datePaint
                );

                canvas.drawText(
                        hijriText,
                        centerX,
                        startY
                                + dateSpacing * 2,
                        datePaint
                );

            } finally {

                if (canvas != null) {
                    holder.unlockCanvasAndPost(
                            canvas
                    );
                }
            }
        }
    }

    // =====================================
    // Gregorian → Jalali
    // =====================================

    private static int[] gregorianToJalali(
            int gy,
            int gm,
            int gd
    ) {

        int[] gDays = {
                31, 28, 31, 30, 31, 30,
                31, 31, 30, 31, 30, 31
        };

        int[] jDays = {
                31, 31, 31, 31, 31, 31,
                30, 30, 30, 30, 30, 29
        };

        int gy2 = gy - 1600;
        int gm2 = gm - 1;
        int gd2 = gd - 1;

        int gDayNo =
                365 * gy2;

        gDayNo +=
                (gy2 + 3) / 4;

        gDayNo -=
                (gy2 + 99) / 100;

        gDayNo +=
                (gy2 + 399) / 400;

        for (int i = 0; i < gm2; i++) {
            gDayNo += gDays[i];
        }

        if (
                gm2 > 1 &&
                (
                        (
                                gy % 4 == 0
                                        && gy % 100 != 0
                        )
                        ||
                        gy % 400 == 0
                )
        ) {
            gDayNo++;
        }

        gDayNo += gd2;

        int jDayNo =
                gDayNo - 79;

        int jNp =
                jDayNo / 12053;

        jDayNo %=
                12053;

        int jy =
                979
                        + 33 * jNp
                        + 4 * (jDayNo / 1461);

        jDayNo %=
                1461;

        if (jDayNo >= 366) {

            jy +=
                    (jDayNo - 1) / 365;

            jDayNo =
                    (jDayNo - 1) % 365;
        }

        int i = 0;

        while (
                i < 11 &&
                jDayNo >= jDays[i]
        ) {

            jDayNo -=
                    jDays[i];

            i++;
        }

        int jm =
                i + 1;

        int jd =
                jDayNo + 1;

        return new int[]{
                jy,
                jm,
                jd
        };
    }

    // =====================================
    // Gregorian → Hijri
    // =====================================

    private static int[] gregorianToHijri(
            int year,
            int month,
            int day
    ) {

        int a =
                (14 - month) / 12;

        int y =
                year + 4800 - a;

        int m =
                month + 12 * a - 3;

        int jd =
                day
                        + (153 * m + 2) / 5
                        + 365 * y
                        + y / 4
                        - y / 100
                        + y / 400
                        - 32045;

        int l =
                jd - 1948440 + 10632;

        int n =
                (l - 1) / 10631;

        l =
                l
                        - 10631 * n
                        + 354;

        int j =
                (
                        ((10985 - l) / 5316)
                                * ((50 * l) / 17719)
                )
                        +
                        (
                                (l / 5670)
                                        * ((43 * l) / 15238)
                        );

        l =
                l
                        - ((30 - j) / 15)
                        * ((17719 * j) / 50)
                        - (j / 16)
                        * ((15238 * j) / 43)
                        + 29;

        int mh =
                (24 * l) / 709;

        int dh =
                l
                        - (709 * mh) / 24;

        int yh =
                30 * n + j - 30;

        return new int[]{
                yh,
                mh,
                dh
        };
    }
        }
