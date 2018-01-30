package it.gabrieletondi.telldontaskkata.domain

import java.math.BigDecimal

class OrderItem {
    var product: Product? = null
    var quantity: Int = 0
    var taxedAmount: BigDecimal? = null
    var tax: BigDecimal? = null
}
