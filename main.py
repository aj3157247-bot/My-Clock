from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.label import Label
from kivy.clock import Clock
from datetime import datetime


# -----------------------------
# Gregorian → Jalali
# -----------------------------
def gregorian_to_jalali(gy, gm, gd):

    g_days = [31, 28, 31, 30, 31, 30,
              31, 31, 30, 31, 30, 31]

    j_days = [31, 31, 31, 31, 31, 31,
              30, 30, 30, 30, 30, 29]

    gy2 = gy - 1600
    gm2 = gm - 1
    gd2 = gd - 1

    g_day_no = 365 * gy2
    g_day_no += (gy2 + 3) // 4
    g_day_no -= (gy2 + 99) // 100
    g_day_no += (gy2 + 399) // 400

    for i in range(gm2):
        g_day_no += g_days[i]

    if gm2 > 1 and (
        (gy % 4 == 0 and gy % 100 != 0)
        or gy % 400 == 0
    ):
        g_day_no += 1

    g_day_no += gd2

    j_day_no = g_day_no - 79

    j_np = j_day_no // 12053
    j_day_no %= 12053

    jy = 979 + 33 * j_np + 4 * (j_day_no // 1461)
    j_day_no %= 1461

    if j_day_no >= 366:
        jy += (j_day_no - 1) // 365
        j_day_no = (j_day_no - 1) % 365

    i = 0

    while i < 11 and j_day_no >= j_days[i]:
        j_day_no -= j_days[i]
        i += 1

    jm = i + 1
    jd = j_day_no + 1

    return jy, jm, jd


# -----------------------------
# Gregorian → Hijri
# -----------------------------
def gregorian_to_hijri(year, month, day):

    a = (14 - month) // 12
    y = year + 4800 - a
    m = month + 12 * a - 3

    jd = (
        day
        + (153 * m + 2) // 5
        + 365 * y
        + y // 4
        - y // 100
        + y // 400
        - 32045
    )

    l = jd - 1948440 + 10632
    n = (l - 1) // 10631

    l = l - 10631 * n + 354

    j = (
        ((10985 - l) // 5316)
        * ((50 * l) // 17719)
        + (l // 5670)
        * ((43 * l) // 15238)
    )

    l = (
        l
        - ((30 - j) // 15)
        * ((17719 * j) // 50)
        - (j // 16)
        * ((15238 * j) // 43)
        + 29
    )

    m = (24 * l) // 709
    d = l - (709 * m) // 24
    y = 30 * n + j - 30

    return y, m, d


# =============================
# APP
# =============================
class DateTimeApp(App):

    def build(self):

        main = BoxLayout(
            orientation="vertical",
            padding=[20, 20, 20, 20],
            spacing=0
        )

        # -------------------------
        # ساعت
        # -------------------------
        self.time_label = Label(
            text="00:00:00",
            font_size="80sp",
            bold=True,
            size_hint_y=0.30
        )

        main.add_widget(self.time_label)


        # -------------------------
        # فضای خالی
        # -------------------------
        top_space = Label(
            text="",
            size_hint_y=0.20
        )

        main.add_widget(top_space)


        # -------------------------
        # تاریخ‌ها
        # -------------------------
        dates = BoxLayout(
            orientation="vertical",
            spacing=-8,
            size_hint_y=0.25
        )

        self.date_label = Label(
            text="1405/01/01",
            font_size="50sp"
        )

        self.gregorian_label = Label(
            text="2026/01/01",
            font_size="50sp"
        )

        self.hijri_label = Label(
            text="1447/01/01",
            font_size="50sp"
        )

        dates.add_widget(self.date_label)
        dates.add_widget(self.gregorian_label)
        dates.add_widget(self.hijri_label)

        main.add_widget(dates)


        # -------------------------
        # فضای خالی پایین
        # -------------------------
        bottom_space = Label(
            text="",
            size_hint_y=0.25
        )

        main.add_widget(bottom_space)


        Clock.schedule_interval(
            self.update_clock,
            1
        )

        self.update_clock(0)

        return main


    # -----------------------------
    # بروزرسانی
    # -----------------------------
    def update_clock(self, dt):

        now = datetime.now()

        self.time_label.text = now.strftime(
            "%I:%M:%P"
        )


        # شمسی
        jy, jm, jd = gregorian_to_jalali(
            now.year,
            now.month,
            now.day
        )

        self.date_label.text = (
            f"{jy}/{jm:02d}/{jd:02d}"
        )


        # میلادی
        self.gregorian_label.text = (
            f"{now.year}/"
            f"{now.month:02d}/"
            f"{now.day:02d}"
        )


        # قمری
        hy, hm, hd = gregorian_to_hijri(
            now.year,
            now.month,
            now.day
        )

        self.hijri_label.text = (
            f"{hy}/{hm:02d}/{hd:02d}"
        )


if __name__ == "__main__":
    DateTimeApp().run()
