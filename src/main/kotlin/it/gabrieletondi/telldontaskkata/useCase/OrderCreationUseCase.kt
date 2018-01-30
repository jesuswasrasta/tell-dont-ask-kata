package it.gabrieletondi.telldontaskkata.useCase

import it.gabrieletondi.telldontaskkata.domain.Order
import it.gabrieletondi.telldontaskkata.domain.OrderItem
import it.gabrieletondi.telldontaskkata.domain.OrderStatus
import it.gabrieletondi.telldontaskkata.repository.OrderRepository
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog

import java.math.BigDecimal
import java.util.ArrayList

import java.math.BigDecimal.valueOf
import java.math.RoundingMode.HALF_UP

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
                val unitaryTax = product.price!!.divide(valueOf(100)).multiply(product.category!!.taxPercentage!!).setScale(2, HALF_UP)
                val unitaryTaxedAmount = product.price!!.add(unitaryTax).setScale(2, HALF_UP)
                val taxedAmount = unitaryTaxedAmount.multiply(BigDecimal.valueOf(itemRequest.quantity.toLong())).setScale(2, HALF_UP)
                val taxAmount = unitaryTax.multiply(BigDecimal.valueOf(itemRequest.quantity.toLong()))

                val orderItem = OrderItem()
                orderItem.product = product
                orderItem.quantity = itemRequest.quantity
                orderItem.tax = taxAmount
                orderItem.taxedAmount = taxedAmount
                order.addItem(orderItem)

                order.total = order.total!!.add(taxedAmount)
                order.tax = order.tax!!.add(taxAmount)
            }
        }

        orderRepository.save(order)
    }
}
