data class Table(val tableNumber: Int, val seats: Int, val seatedCustomers: Set<Customer>)

data class Menu(val dishes: List<Dish>)

data class Dish(val name: String, val cost: Int)

data class Order(val dish: Dish)

data class Meal(val dish: Dish)

data class Bill(val orders: List<Order>)
