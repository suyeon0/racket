rootProject.name = "racket"
include("apps")
include("apps:api-interface")
findProject(":apps:api-interface")?.name = "api-interface"
include("apps:api-interface:user")
findProject(":apps:api-interface:user")?.name = "user"
