package it.gabrieletondi.telldontaskkata.useCase

import it.gabrieletondi.telldontaskkata.domain.Category
import it.gabrieletondi.telldontaskkata.domain.OrderItem
import it.gabrieletondi.telldontaskkata.domain.OrderStatus
import it.gabrieletondi.telldontaskkata.domain.Product
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class OrderCreationUseCaseTest {
    private val orderRepository = TestOrderRepository()
    private val food = object : Category() {
        init {
            name = "food"
            taxPercentage = BigDecimal("10")
        }
    }
    private val productCatalog = InMemoryProductCatalog(
            Arrays.asList(
                    object : Product() {
                        init {
                            name = "salad"
                            price = BigDecimal("3.56")
                            category = food
                        }
                    },
                    object : Product() {
                        init {
                            name = "tomato"
                            price = BigDecimal("4.65")
                            category = food
                        }
                    }
            )
    )
    private val useCase = OrderCreationUseCase(orderRepository, productCatalog)

    @Test
    @Throws(Exception::class)
    fun sellMultipleItems() {
        val saladRequest = SellItemRequest()
        saladRequest.productName = "salad"
        saladRequest.quantity = 2

        val tomatoRequest = SellItemRequest()
        tomatoRequest.productName = "tomato"
        tomatoRequest.quantity = 3

        val request = SellItemsRequest()
        request.requests = ArrayList()
        request.requests!!.add(saladRequest)
        request.requests!!.add(tomatoRequest)

        useCase.run(request)

        val insertedOrder = orderRepository.savedOrder
        assertThat(insertedOrder!!.status, `is`(OrderStatus.CREATED))
        assertThat(insertedOrder.total, `is`(BigDecimal("23.20")))
        assertThat(insertedOrder.tax, `is`(BigDecimal("2.13")))
        assertThat(insertedOrder.currency, `is`("EUR"))
        assertThat<List<OrderItem>>(insertedOrder.items, hasSize<OrderItem>(2))
        assertThat(insertedOrder.items[0].product!!.name, `is`("salad"))
        assertThat(insertedOrder.items[0].product!!.price, `is`(BigDecimal("3.56")))
        assertThat(insertedOrder.items[0].quantity, `is`(2))
        assertThat(insertedOrder.items[0].taxedAmount, `is`(BigDecimal("7.84")))
        assertThat(insertedOrder.items[0].tax, `is`(BigDecimal("0.72")))
        assertThat(insertedOrder.items[1].product!!.name, `is`("tomato"))
        assertThat(insertedOrder.items[1].product!!.price, `is`(BigDecimal("4.65")))
        assertThat(insertedOrder.items[1].quantity, `is`(3))
        assertThat(insertedOrder.items[1].taxedAmount, `is`(BigDecimal("15.36")))
        assertThat(insertedOrder.items[1].tax, `is`(BigDecimal("1.41")))
    }

    @Test(expected = UnknownProductException::class)
    @Throws(Exception::class)
    fun unknownProduct() {
        val request = SellItemsRequest()
        request.requests = ArrayList()
        val unknownProductRequest = SellItemRequest()
        unknownProductRequest.productName = "unknown product"
        request.requests!!.add(unknownProductRequest)

        useCase.run(request)
    }
}
