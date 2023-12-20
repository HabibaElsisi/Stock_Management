package handlers

import java.io._
import scala.io.Source
import models.Product


object ProductFormatter {
  def formatProductLine(productLine: String): String = {
    val productRegex = """Product\((\d+),(.+),(\d+),(\d+\.\d+),(.+)\)""".r
    productLine match {
      case productRegex(id, name, quantity, price, supplier) =>
        s"$id,$name,$quantity,$price,$supplier"
      case _ => productLine
    }
  }
}

class FileHandler(filePath: String) {
  private val inventoryFile: File = new File(filePath)
  private val tempFilePath = "src/main/resources/temp_inventory.txt"
  // Method to add a product to the file
  def addProductToFile(product: Product): Unit = {
    val tempFile = new File(tempFilePath)
    val lines = Source.fromFile(tempFile).getLines().toList

    // Find the maximum ID in the current products
    val lastId = lines
      .map(_.split(",")(0).toIntOption)
      .collect { case Some(id) => id }
      .maxOption.getOrElse(0)

    // Increment the ID for the new product
    val newId = (lastId + 1).toString

    val writer = new BufferedWriter(new FileWriter(tempFile, true))
    writer.write(s"$newId,${product.name},${product.quantity},${product.price},${product.supplier}\n")
    writer.close()
  }


//  private def sortProductsById(): Unit = {
//    try {
//      val tempFile = new File(tempFilePath)
//      val lines = scala.io.Source.fromFile(tempFilePath).getLines().toList
//
//      val sortedLines = lines.sortBy(line => line.split(",")(0).toInt) // Sort by ID
//      val writer = new BufferedWriter(new FileWriter(tempFile))
//      sortedLines.foreach { line =>
//        writer.write(line + "\n")
//      }
//      writer.close()
//    } catch {
//      case e: Exception =>
//        println(s"An error occurred while sorting products by ID: ${e.getMessage}")
//    }
//  }


  //  import java.io.{File, FileWriter, BufferedWriter}
//  import scala.io.Source

  // Assuming tempFilePath is the path of the file you want to update



  def deleteProduct(productId: String): Unit = {
    val filePath = "src/main/resources/temp_inventory.txt"

    try {
      val tempFile = new File(filePath)
      val lines = scala.io.Source.fromFile(filePath).getLines().toList

      val writer = new BufferedWriter(new FileWriter(tempFile))
      var productFound = false // Flag to track if the product was found

      lines.foreach { line =>
        val productInfo = line.split(",")
        if (productInfo.head == productId) {
          productFound = true
          // Do not write the line if it matches the productId (i.e., delete the line)
        } else {
          writer.write(ProductFormatter.formatProductLine(line) + "\n")
        }
      }
      writer.close()

      if (productFound) {
        println(s"Product with ID $productId deleted successfully ")
      } else {
        println(s"Product with ID $productId not found ")
      }

    } catch {
      case e: Exception =>
        println(s"An error occurred while deleting the product: ${e.getMessage}")
    }
  }


  import java.io.{File, FileWriter, BufferedWriter}

  def updateProduct(productId: String, updatedProduct: String): Unit = {
    val filePath = "src/main/resources/temp_inventory.txt"

    try {
      val tempFile = new File(filePath)
      val lines = scala.io.Source.fromFile(filePath).getLines().toList

      val writer = new BufferedWriter(new FileWriter(tempFile))
      lines.foreach { line =>
        val productInfo = line.split(",")
        if (productInfo.head == productId) {
          val updatedLine = ProductFormatter.formatProductLine(updatedProduct)
          writer.write(updatedLine + "\n") // Write the updated product
        } else {
          writer.write(ProductFormatter.formatProductLine(line) + "\n") // Write other lines as they are
        }
      }
      writer.close()
    } catch {
      case e: Exception =>
        println(s"An error occurred while updating the file: ${e.getMessage}")
    }
  }

  // Method to update a product in the file by ID
  import java.io.{File, FileWriter, BufferedWriter}

//  def updateProduct(productId: String, updatedProduct: String): Unit = {
//    val filePath = "src/main/resources/temp_inventory.txt"
//
//    try {
//      val tempFile = new File(filePath)
//      val lines = scala.io.Source.fromFile(filePath).getLines().toList
//
//      val writer = new BufferedWriter(new FileWriter(tempFile))
//      lines.foreach { line =>
//        val idRegex = """ID: (\d+),.*""".r
//        line match {
//          case idRegex(id) if id == productId =>
//            val updatedLine = ProductFormatter.formatProductLine(updatedProduct)
//            writer.write(updatedLine + "\n") // Write the updated product in the desired format
//          case _ =>
//            writer.write(ProductFormatter.formatProductLine(line) + "\n") // Write other lines in the desired format
//        }
//      }
//      writer.close()
//    } catch {
//      case e: Exception =>
//        println(s"An error occurred while updating the file: ${e.getMessage}")
//    }
//  }


  // Function to reformat data (adjust the format as needed)
//  def reformatData(line: String): String = {
//    val data = line.split(",")
//    // Example reformatting logic:
//    // Assuming each line has at least 5 elements
//    if (data.length >= 5) {
//      // Reformat the line by reconstructing it based on the assumed structure
//      s"${data(0)},${data(1)},${data(2)},${data(3)},${data(4)}"
//    } else {
//      line // Return line as it is if no reformatting needed
//    }
//  }
//
//  // Function to reformat the entire file
//  def reformatFile(filePath: String): Unit = {
//    try {
//      val file = new File(filePath)
//      val lines = scala.io.Source.fromFile(filePath).getLines().toList
//
//      val writer = new BufferedWriter(new FileWriter(file))
//      lines.foreach { line =>
//        writer.write(reformatData(line) + "\n") // Write each line with reformatted data
//      }
//      writer.close()
//    } catch {
//      case e: Exception =>
//        println(s"An error occurred while reformatting the file: ${e.getMessage}")
//    }
//  }




  // Method to retrieve product details by ID
  def retrieveProduct(productId: String): Option[Product] = {
    val products = Source.fromFile(tempFilePath).getLines().map { line =>
      val Array(id, name, quantity, price, supplier) = line.split(",")
      Product(id, name, quantity.toInt, price.toDouble, supplier)
    }.toList

    val product = products.find(_.id == productId)
    product.foreach(checkQuantityAlert)
    product
  }


  private def checkQuantityAlert(product: Product): Unit = {
    val lowQuantityThreshold = 20 // Define your threshold here
    if (product.quantity >= 0 && product.quantity <= lowQuantityThreshold) {
      println(s"Alert: The quantity of ${product.name} is low. Remaining quantity: ${product.quantity}")
      println(s"'low quantity' add extension for ${product.name}:")
      val input = scala.io.StdIn.readInt()
      extendProductQuantity(product.id, input)
    }
  }

  def extendProductQuantity(productId: String, additionalQuantity: Int): Unit = {
    val tempFile = new File(tempFilePath)
    val lines = Source.fromFile(tempFile).getLines().toList

    val writer = new BufferedWriter(new FileWriter(tempFile))
    lines.foreach { line =>
      val productInfo = line.split(",")
      if (productInfo.head == productId) {
        val currentQuantity = productInfo(2).toInt // Assuming the quantity is at the third position
        val newQuantity = currentQuantity + additionalQuantity
        writer.write(s"${productInfo.head},${productInfo(1)},$newQuantity,${productInfo(3)},${productInfo(4)}\n")
      } else {
        writer.write(line + "\n")
      }
    }
    writer.close()
  }


  // Method to read all products from the file
  def getAllProducts: List[Product] = {
    Source.fromFile(tempFilePath).getLines().map { line =>
      val Array(id, name, quantity, price, supplier) = line.split(",")
      Product(id, name, quantity.toInt, price.toDouble, supplier)
    }.toList
  }
}
