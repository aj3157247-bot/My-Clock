[app]

# -----------------------------
# اطلاعات برنامه
# -----------------------------
title = My Clock
package.name = myclock
package.domain = org.example

# -----------------------------
# فایل‌های پروژه
# -----------------------------
source.dir = .
source.include_exts = py,png,jpg,jpeg,kv,atlas

# -----------------------------
# Python + Kivy
# -----------------------------
requirements = python3==3.12.10,kivy==2.3.1

# -----------------------------
# تنظیمات برنامه
# -----------------------------
orientation = portrait
version = 1.0

# -----------------------------
# Android
# -----------------------------
android.api = 35
android.minapi = 24
android.archs = arm64-v8a

android.accept_sdk_license = True

# -----------------------------
# تنظیمات Buildozer
# -----------------------------
log_level = 2
