rootProject.name = "demo"
include("shadow-local")

include(":disposable-email")
project(":disposable-email").projectDir = File(settingsDir, "../disposable-email")

