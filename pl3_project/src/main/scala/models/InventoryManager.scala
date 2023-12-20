package models
import java.io.{BufferedWriter, File, FileWriter, IOException}
import handlers.FileHandler

class InventoryManager(fileHandler: FileHandler) {
  // Method to add a product to the inventory
  def addProduct(product: Product): Unit = {
    fileHandler.addProductToFile(product)
  }

  // Method to delete a product from the inventory by ID
  def deleteProduct(productId: String): Unit = {
    fileHandler.deleteProduct(productId)
  }

  // Method to update a product in the inventory by ID
  def updateProduct(productId: String, updatedProduct: Product): Unit = {
    try {
      // Call the updateProduct method in the fileHandler with the correct arguments
      fileHandler.updateProduct(productId, updatedProduct.toString) // Assuming updatedProduct is a Product object

      println("Product updated successfully.")
    } catch {
      case e: IOException =>
        println("An error occurred while updating the product: " + e.getMessage)
    }
  }


  // Method to retrieve product details by ID from the inventory
  def retrieveProduct(productId: String): Option[Product] = {
    fileHandler.retrieveProduct(productId)
  }

  // Method to get all products from the inventory
  def getAllProducts: List[Product] = {
    fileHandler.getAllProducts
  }
}
