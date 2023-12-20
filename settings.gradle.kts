rootProject.name = "racket"

include("api-interface")
include("api-interface:api-shared:user")
include("api-interface:api-shared:payment")

include("consumer-interface")

include("shared:domain")
include("shared:util")