include(":app")
include(":annotation")
include(":processor")
include(":CommonTools")
include(":flutter_transform_plugin")

project(":CommonTools").projectDir = file("submodules/CommonTools")
project(":processor").projectDir = file("submodules/processor")
project(":annotation").projectDir = file("submodules/annotation")
project(":flutter_transform_plugin").projectDir = file("submodules/flutter_transform_plugin")