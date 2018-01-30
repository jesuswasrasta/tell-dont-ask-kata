package it.gabrieletondi.telldontaskkata.domain

import java.math.BigDecimal

class Order {
    var total: BigDecimal? = null
    var currency: String? = null
    var items: MutableList<OrderItem> = mutableListOf()
    var tax: BigDecimal? = null
    var status: OrderStatus? = null
    var id: Int = 0
    fun addItem(item: OrderItem) {
        items.add(item)
    }
}
