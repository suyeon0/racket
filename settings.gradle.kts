rootProject.name = "racket"

include("api-interface")
include("api-interface:cash-process")
findProject(":api-interface:cash-process")?.name = "cash-process"
include("api-interface:cash-process:cash")
findProject(":api-interface:cash-process:cash")?.name = "cash"
include("api-interface:cash-process:cash-domain")
findProject(":api-interface:cash-process:cash-domain")?.name = "cash-domain"
include("api-interface:cash-process:cash-consumer")
findProject(":api-interface:cash-process:cash-consumer")?.name = "cash-consumer"
include("api-interface:api-shared")
findProject(":api-interface:api-shared")?.name = "api-shared"
include("api-interface:api-shared:user")
findProject(":api-interface:api-shared:user")?.name = "user"
include("shared")
include("shared:domain")
findProject(":shared:domain")?.name = "domain"
include("api-interface:api-shared:payment")
findProject(":api-interface:api-shared:payment")?.name = "payment"
