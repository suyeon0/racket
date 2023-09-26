rootProject.name = "racket"

include("api-interface")

include("api-interface:cash-process:cash")
include("api-interface:cash-process:cash-domain")

include("api-interface:api-shared:user")
include("api-interface:api-shared:payment")

include("shared:domain")
include("shared:util")

include("consumer-interface:cash-consumer")
include("api-interface:delivery")
include("api-interface:cart")