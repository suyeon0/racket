rootProject.name = "racket"

include("api-interface")
include("api-interface:api-shared:user")
include("api-interface:api-shared:payment")

include("shared:domain")
include("shared:util")

include("consumer-interface:cash-consumer")
include("consumer-interface:delivery-consumer")