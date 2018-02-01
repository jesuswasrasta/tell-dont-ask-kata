package it.gabrieletondi.telldontaskkata.useCase

import it.gabrieletondi.telldontaskkata.domain.Order
import it.gabrieletondi.telldontaskkata.domain.OrderItem
import it.gabrieletondi.telldontaskkata.domain.OrderStatus
import it.gabrieletondi.telldontaskkata.repository.OrderRepository
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog
import java.math.BigDecimal

class OrderCreationUseCase(private val orderRepository: OrderRepository, private val productCatalog: ProductCatalog) {

    fun run(request: SellItemsRequest) {
        val order = Order()
        order.status = OrderStatus.CREATED
        order.currency = "EUR"
        order.total = BigDecimal("0.00")
        order.tax = BigDecimal("0.00")

        for (itemRequest in request.requests!!) {
            val product = productCatalog.getByName(itemRequest.productName)

            if (product == null) {
                throw UnknownProductException()
            } else {


                val orderItem = OrderItem()
                orderItem.product = product
                orderItem.quantity = itemRequest.quantity
                order.addItem(orderItem)


            }
        }

        orderRepository.save(order)
    }
}
