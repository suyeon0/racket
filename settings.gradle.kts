rootProject.name = "racket"

include("api-interface")
include("module-stream")
include("module-stream:kafka-producer")
findProject(":module-stream:kafka-producer")?.name = "kafka-producer"
include("module-stream:kafka-consumer")
findProject(":module-stream:kafka-consumer")?.name = "kafka-consumer"
include("api-interface:cache-process")
findProject(":api-interface:cache-process")?.name = "cache-process"
include("api-interface:cache-process")
findProject(":api-interface:cache-process")?.name = "cache-process"
include("api-interface:cache-process:domain")
findProject(":api-interface:cache-process:domain")?.name = "domain"
include("api-interface:cache-process:cache-consumer")
findProject(":api-interface:cache-process:cache-consumer")?.name = "cache-consumer"
include("api-interface:cache-process:cache")
findProject(":api-interface:cache-process:cache")?.name = "cache"
