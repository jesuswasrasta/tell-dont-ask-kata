package it.gabrieletondi.telldontaskkata.useCase

import it.gabrieletondi.telldontaskkata.domain.Order
import it.gabrieletondi.telldontaskkata.domain.OrderStatus
import it.gabrieletondi.telldontaskkata.repository.OrderRepository
import it.gabrieletondi.telldontaskkata.service.ShipmentService

import it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED
import it.gabrieletondi.telldontaskkata.domain.OrderStatus.REJECTED
import it.gabrieletondi.telldontaskkata.domain.OrderStatus.SHIPPED

class OrderShipmentUseCase(private val orderRepository: OrderRepository, private val shipmentService: ShipmentService) {

    fun run(request: OrderShipmentRequest) {
        val order = orderRepository.getById(request.orderId)

        if (order.status == CREATED || order.status == REJECTED) {
            throw OrderCannotBeShippedException()
        }

        if (order.status == SHIPPED) {
            throw OrderCannotBeShippedTwiceException()
        }

        shipmentService.ship(order)

        order.status = OrderStatus.SHIPPED
        orderRepository.save(order)
    }
}
