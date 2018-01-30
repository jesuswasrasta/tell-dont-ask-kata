package it.gabrieletondi.telldontaskkata.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class OrderTest{

    @Test
    fun `add item adds an item`() {
        val item = OrderItem()
        item.product = Product()

        val order = Order()
        order.addItem(item)

        assertEquals(1, order.items.size)
    }

    @Test
    fun `add item `() {
        val item = OrderItem()
        item.product = Product()

        val order = Order()
        order.addItem(item)

        assertEquals(1, order.items.size)
    }
}