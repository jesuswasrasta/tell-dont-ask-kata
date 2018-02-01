package it.gabrieletondi.telldontaskkata.domain

import java.math.BigDecimal
import java.math.RoundingMode

class OrderItem {
    var product: Product? = null
    var quantity: Int = 0
    val taxedAmount: BigDecimal?
        get() = product?.unitaryTaxedAmount?.multiply(BigDecimal.valueOf(this.quantity.toLong()))?.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
    val tax: BigDecimal?
        get() = product?.unitaryTax?.multiply(BigDecimal.valueOf(this.quantity.toLong()))
}
