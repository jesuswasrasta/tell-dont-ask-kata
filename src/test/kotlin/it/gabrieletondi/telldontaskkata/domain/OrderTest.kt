package it.gabrieletondi.telldontaskkata.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class OrderTest{

    @Test
    fun `add item adds an item`() {
        val item = OrderItem()
        val product = Product()
        val category = Category()
        category.name = "fruits"
        category.taxPercentage = BigDecimal(4)
        product.category = category
        item.product = product

        val order = Order()
        order.addItem(item)

        assertEquals(1, order.items.size)
    }


    @Test
    fun `add item increases the order total`() {
        val item = OrderItem()
        item.quantity = 1
        val product = Product()
        val category = Category()
        category.name = "fruits"
        category.taxPercentage = BigDecimal(10)
        product.category = category
        product.price = BigDecimal(100)
        item.product = product

        val order = Order()
        order.addItem(item)

        assertEquals(BigDecimal(110).setScale(2, RoundingMode.HALF_UP), order.total)
    }
}