package org.example.myclock;

import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ClockWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new ClockEngine();
    }

    private class ClockEngine extends Engine {

        private Timer timer;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private boolean visible = false;

        ClockEngine() {
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        public void onVisibilityChanged(boolean isVisible) {
            visible = isVisible;

            if (isVisible) {
                startClock();
            } else {
                stopClock();
            }
        }

        @Override
        public void onSurfaceChanged(
                SurfaceHolder holder,
                int format,
                int width,
                int height) {

            drawClock();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            stopClock();
            super.onSurfaceDestroyed(holder);
        }

        private void startClock() {
            stopClock();

            timer = new Timer();

            timer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            drawClock();
                        }
                    },
                    0,
                    1000
            );
        }

        private void stopClock() {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
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

                Date now = new Date();

                SimpleDateFormat timeFormat =
                        new SimpleDateFormat(
                                "HH:mm",
                                Locale.getDefault()
                        );

                SimpleDateFormat secondsFormat =
                        new SimpleDateFormat(
                                "ss",
                                Locale.getDefault()
                        );

                String time =
                        timeFormat.format(now);

                String seconds =
                        secondsFormat.format(now);

                paint.setColor(Color.rgb(180, 80, 255));

                paint.setTextSize(
                        Math.min(width, height) * 0.20f
                );

                canvas.drawText(
                        time,
                        width / 2f,
                        height / 2f,
                        paint
                );

                paint.setTextSize(
                        Math.min(width, height) * 0.055f
                );

                canvas.drawText(
                        seconds,
                        width / 2f,
                        height / 2f
                                + Math.min(width, height) * 0.09f,
                        paint
                );

            } finally {

                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
