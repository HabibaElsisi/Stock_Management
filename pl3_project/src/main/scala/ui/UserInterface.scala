package ui

import models.{Product, InventoryManager}
import reports.ReportGenerator

import scala.io.StdIn

class UserInterface(inventoryManager: InventoryManager, reportGenerator: ReportGenerator) {
  private val filePath = "src/main/resources/product_report.txt" // Path to your report file
  private var isAdminLoggedIn: Boolean = false

  // Authenticate admin with username and password
  private def authenticateAdmin(): Unit = {
    val username = "admin" // Replace with actual admin username
    val password = "1234" // Replace with actual admin password

    print("Enter username: ")
    val inputUsername = StdIn.readLine()

    print("Enter password: ")
    val inputPassword = StdIn.readLine()

    isAdminLoggedIn = inputUsername == username && inputPassword == password
    if (isAdminLoggedIn) {
      println("Admin logged in successfully.")
    } else {
      println("Invalid credentials.")
    }
  }

  private def getUserInput(prompt: String): String = {
    print(prompt + " ")
    StdIn.readLine()
  }

  private def getUserInputAsInt(prompt: String): Int = {
    try {
      getUserInput(prompt).toInt
    } catch {
      case _: NumberFormatException =>
        println("Please enter a valid number.")
        getUserInputAsInt(prompt)
    }
  }

  private def displayMenu(isAdmin: Boolean): Unit = {
    println("Stock Management System")
    println("1. View Product Details")
    println("2. View All Products")
    if (isAdmin) {
      println("3. Add Product")
      println("4. Update Product")
      println("5. Delete Product")
      println("6. Generate Product Report")
    }
    println("0. Exit")
  }

  private def addProduct(): Unit = {
    println("Adding a product:")

    val productId = getUserInput("Enter product ID: ")
    val productName = getUserInput("Enter product name: ")
    val productQuantity = getUserInputAsInt("Enter product quantity: ")
    val productPrice = getUserInput("Enter product price: ").toDouble
    val productSupplier = getUserInput("Enter product supplier: ")

    // Create a Product instance
    val newProduct = Product(productId, productName, productQuantity, productPrice, productSupplier)

    // Add the product using InventoryManager
    inventoryManager.addProduct(newProduct)
    println("Product added successfully.")
  }


  private def deleteProduct(): Unit = {
    val productId = getUserInput("Enter the product ID to delete: ")

    // Delete the product using InventoryManager
    inventoryManager.deleteProduct(productId)
    //println("Product deleted successfully.")
  }


  private def updateProduct(): Unit = {
    val productId = getUserInput("Enter the product ID to update: ")
    val existingProduct = inventoryManager.retrieveProduct(productId)

    existingProduct match {
      case Some(product) =>
        println(s"Updating product with ID: $productId")
        println("Choose an option to update:")
        println("1. Update Name")
        println("2. Update Quantity")
        println("3. Update Price")
        println("4. Update Supplier")
        println("0. Go back")

        val choice = getUserInputAsInt("Enter your choice: ")

        choice match {
          case 1 =>
            val newName = getUserInput("Enter the new name: ")
            val updatedProduct = product.copy(name = newName)
            inventoryManager.updateProduct(productId, updatedProduct)
          case 2 =>
            val newQuantity = getUserInputAsInt("Enter the new quantity: ")
            val updatedProduct = product.copy(quantity = newQuantity)
            inventoryManager.updateProduct(productId, updatedProduct)
          case 3 =>
            val newPrice = getUserInput("Enter the new price: ").toDouble
            val updatedProduct = product.copy(price = newPrice)
            inventoryManager.updateProduct(productId, updatedProduct)
          case 4 =>
            val newSupplier = getUserInput("Enter the new supplier: ")
            val updatedProduct = product.copy(supplier = newSupplier)
            inventoryManager.updateProduct(productId, updatedProduct)
          case 0 =>
            println("Going back to the main menu.")
          case _ =>
            println("Invalid choice.")
        }

      case None =>
        println("Product not found.")
    }
  }


  private def viewProductDetails(): Unit = {
    val productId = getUserInput("Enter the product ID: ")

    // Retrieve product details using InventoryManager
    val productDetails = inventoryManager.retrieveProduct(productId)

    productDetails match {
      case Some(product) =>
        // Display the product details
        println("Product Details:")
        println(s"ID: ${product.id}")
        println(s"Name: ${product.name}")
        println(s"Quantity: ${product.quantity}")
        println(s"Price: ${product.price}")
        println(s"Supplier: ${product.supplier}")
      case None =>
        println("Product not found.")
    }
  }


  private def viewAllProducts(): Unit = {
    val allProducts = inventoryManager.getAllProducts
    if (allProducts.nonEmpty) {
      println("All Products:")
      allProducts.foreach { product =>
        println(s"ID: ${product.id}, Name: ${product.name}, Quantity: ${product.quantity}, Price: ${product.price}, Supplier: ${product.supplier}")
      }
    } else {
      println("No products found.")
    }
  }

  private def generateReport(): Unit = {
    val allProducts = inventoryManager.getAllProducts
    if (allProducts.nonEmpty) {
      val reportFilePath = "src/main/resources/product_report.txt" // Path for the report file

      // Generate the report using the ReportGenerator class
      reportGenerator.generateProductReport(allProducts, reportFilePath)
      println(s"Product report generated successfully. Path: $reportFilePath")
    } else {
      println("No products found. Cannot generate a report.")
    }
  }

  def startInterface(): Unit = {
    authenticateAdmin()
    if (isAdminLoggedIn) {
      var choice = -1
      while (choice != 0) {
        displayMenu(isAdminLoggedIn)
        choice = getUserInputAsInt("Enter your choice:")
        choice match {
          case 1 => viewProductDetails()
          case 2 => viewAllProducts()
          case 3 if isAdminLoggedIn => addProduct()
          case 4 if isAdminLoggedIn => updateProduct()
          case 5 if isAdminLoggedIn => deleteProduct()
          case 6 if isAdminLoggedIn => generateReport()
          case 0 => println("Exiting Stock Management System. Goodbye!")
          case _ => println("Invalid choice. Please enter a valid option.")
        }
      }
    } else {
      var choice = -1
      while (choice != 0) {
        displayMenu(isAdminLoggedIn)
        choice = getUserInputAsInt("Enter your choice:")
        choice match {
          case 1 => viewProductDetails()
          case 2 => viewAllProducts()
          case 0 => println("Exiting Stock Management System. Goodbye!")
          case _ => println("Invalid choice. Please enter a valid option.")
        }
      }
    }
  }
}