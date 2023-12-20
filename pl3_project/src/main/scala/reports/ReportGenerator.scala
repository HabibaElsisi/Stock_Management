package reports

import models.Product
import java.io.PrintWriter
import java.io.File

class ReportGenerator {

  // Method to generate a report of all products and save it to a file
  def generateProductReport(products: List[Product], filePath: String): Unit = {
    val reportFile = new File(filePath)
    val writer = new PrintWriter(reportFile)

    if (products.nonEmpty) {
      writer.println("Product Report")
      writer.println("ID, Name, Quantity, Price, Supplier")
      products.foreach { product =>
        writer.println(s"${product.id}, ${product.name}, ${product.quantity}, ${product.price}, ${product.supplier}")
      }
      writer.close()
      println(s"Product report generated successfully. Path: $filePath")
    } else {
      writer.println("No products found.")
      writer.close()
      println("No products to generate report.")
    }
  }
}

