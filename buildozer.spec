[app]

title = My Clock
package.name = myclock
package.domain = org.example

source.dir = .
source.include_exts = py,png,jpg,jpeg,kv,atlas

version = 1.0

requirements = python3,kivy

orientation = portrait

fullscreen = 0

android.api = 35
android.minapi = 24
android.archs = arm64-v8a

android.accept_sdk_license = True

android.ndk = 27c

android.enable_androidx = True

android.add_src =

android.permissions =

p4a.branch = master

log_level = 2


[buildozer]

log_level = 2
warn_on_root = 1
