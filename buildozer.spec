[app]

title = My Clock
package.name = myclock
package.domain = org.example

source.dir = .
source.include_exts = py,png,jpg,jpeg,kv,atlas

requirements = python3==3.11.9,hostpython3==3.11.9,kivy==2.3.1

orientation = portrait
version = 1.0

android.api = 35
android.minapi = 24
android.ndk = 27c
android.ndk_api = 24
android.archs = arm64-v8a

android.accept_sdk_license = True
android.enable_androidx = True

p4a.branch = master

android.add_src = %(source.dir)s/android/src
android.add_resources = %(source.dir)s/android/res
android.extra_manifest_application_arguments = %(source.dir)s/android/extra_manifest_application_arguments.xml

log_level = 2
