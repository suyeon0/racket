import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.racket.delivery.consume"])
class DeliveryConsumerApplication {
}

fun main(args: Array<String>) {
	runApplication<DeliveryConsumerApplication>(*args)
}