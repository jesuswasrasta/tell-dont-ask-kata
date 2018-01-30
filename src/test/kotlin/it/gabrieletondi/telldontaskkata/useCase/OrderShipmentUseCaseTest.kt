package it.gabrieletondi.telldontaskkata.useCase

import it.gabrieletondi.telldontaskkata.domain.Order
import it.gabrieletondi.telldontaskkata.domain.OrderStatus
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository
import it.gabrieletondi.telldontaskkata.doubles.TestShipmentService
import org.junit.Test

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat

class OrderShipmentUseCaseTest {
    private val orderRepository = TestOrderRepository()
    private val shipmentService = TestShipmentService()
    private val useCase = OrderShipmentUseCase(orderRepository, shipmentService)

    @Test
    @Throws(Exception::class)
    fun shipApprovedOrder() {
        val initialOrder = Order()
        initialOrder.id = 1
        initialOrder.status = OrderStatus.APPROVED
        orderRepository.addOrder(initialOrder)

        val request = OrderShipmentRequest()
        request.orderId = 1

        useCase.run(request)

        assertThat(orderRepository.savedOrder!!.status, `is`(OrderStatus.SHIPPED))
        assertThat(shipmentService.shippedOrder, `is`(initialOrder))
    }

    @Test(expected = OrderCannotBeShippedException::class)
    @Throws(Exception::class)
    fun createdOrdersCannotBeShipped() {
        val initialOrder = Order()
        initialOrder.id = 1
        initialOrder.status = OrderStatus.CREATED
        orderRepository.addOrder(initialOrder)

        val request = OrderShipmentRequest()
        request.orderId = 1

        useCase.run(request)

        assertThat(orderRepository.savedOrder, `is`(nullValue()))
        assertThat(shipmentService.shippedOrder, `is`(nullValue()))
    }

    @Test(expected = OrderCannotBeShippedException::class)
    @Throws(Exception::class)
    fun rejectedOrdersCannotBeShipped() {
        val initialOrder = Order()
        initialOrder.id = 1
        initialOrder.status = OrderStatus.REJECTED
        orderRepository.addOrder(initialOrder)

        val request = OrderShipmentRequest()
        request.orderId = 1

        useCase.run(request)

        assertThat(orderRepository.savedOrder, `is`(nullValue()))
        assertThat(shipmentService.shippedOrder, `is`(nullValue()))
    }

    @Test(expected = OrderCannotBeShippedTwiceException::class)
    @Throws(Exception::class)
    fun shippedOrdersCannotBeShippedAgain() {
        val initialOrder = Order()
        initialOrder.id = 1
        initialOrder.status = OrderStatus.SHIPPED
        orderRepository.addOrder(initialOrder)

        val request = OrderShipmentRequest()
        request.orderId = 1

        useCase.run(request)

        assertThat(orderRepository.savedOrder, `is`(nullValue()))
        assertThat(shipmentService.shippedOrder, `is`(nullValue()))
    }
}
