package it.gabrieletondi.telldontaskkata.domain

import java.math.BigDecimal

class Order {
    var total: BigDecimal = BigDecimal.ZERO
    var currency: String? = null
    var items: MutableList<OrderItem> = mutableListOf()
    var tax: BigDecimal = BigDecimal.ZERO
    var status: OrderStatus? = null
    var id: Int = 0

    fun addItem(item: OrderItem) {
        items.add(item)
        total = total.add(item.taxedAmount)
        tax = tax.add(item.tax)
    }
}
