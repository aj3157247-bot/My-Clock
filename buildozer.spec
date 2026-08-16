[app]

title = My Clock
package.name = myclock
package.domain = org.example

source.dir = .
source.include_exts = py,png,jpg,jpeg,kv,atlas

requirements = python3,kivy,pyjnius

orientation = portrait
version = 1.0

android.api = 35
android.minapi = 24
android.archs = arm64-v8a

android.accept_sdk_license = True

log_level = 2


[buildozer]

log_level = 2
