package models

case class Product(id: String, name: String, quantity: Int, price: Double, supplier: String) {
  def toFileString: String = s"$id,$name,$quantity,$price,$supplier"
}

