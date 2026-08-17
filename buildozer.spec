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
android.ndk_api = 24

android.archs = arm64-v8a

android.accept_sdk_license = True

android.allow_backup = True

android.entrypoint = org.kivy.android.PythonActivity

p4a.bootstrap = sdl2

log_level = 2
