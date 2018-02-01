package it.gabrieletondi.telldontaskkata.domain

import java.math.BigDecimal
import java.math.RoundingMode

open class Product {
    var name: String? = null
    var price: BigDecimal = BigDecimal.ZERO
    var category: Category? = null
    val unitaryTax: BigDecimal
        get() {
            return this.price.divide(BigDecimal.valueOf(100)).multiply(this.category!!.taxPercentage!!).setScale(2, RoundingMode.HALF_UP)
        }
    val unitaryTaxedAmount: BigDecimal
        get() {
            return this.price.add(unitaryTax).setScale(2, RoundingMode.HALF_UP)
        }
}
