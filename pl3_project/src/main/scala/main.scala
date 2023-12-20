import models.Product
import handlers.FileHandler
import reports.ReportGenerator
import ui.UserInterface
import models.InventoryManager

object Main {
  def main(args: Array[String]): Unit = {
    val filePath = "src/main/resources/temp_inventory.txt" // Path to your inventory file
    val fileHandler = new FileHandler(filePath)
    val inventoryManager = new InventoryManager(fileHandler)
    val reportGenerator = new ReportGenerator()
    val userInterface = new UserInterface(inventoryManager, reportGenerator)

    userInterface.startInterface()
  }
}
