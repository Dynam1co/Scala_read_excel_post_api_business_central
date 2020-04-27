import java.io.File
import java.text.SimpleDateFormat

import com.google.gson.Gson
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{BasicCredentialsProvider, HttpClientBuilder}
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}

import scala.collection.mutable.ListBuffer

object Utils {
  case class LinAlbaran (
                          is_return: Boolean,
                          receipt_no: String,
                          vendor_no: String,
                          item_no: String,
                          quantity: Double,
                          unit_cost: Double,
                          percent_discount: Double,
                          amount: Double,
                          vat_amount: Double,
                          receipt_date: String,
                          order_no: String,
                          ceco: String,
                          entry_type: String
                        )

  def readExcel(): XSSFSheet = {
    val file = new File("src/main/resources/prueba3.xlsx")

    // Abro el libro excel
    val workbook = new XSSFWorkbook(file)

    // Obtengo primera hoja
    val sheet = workbook.getSheetAt(0)

    sheet
  }

  def dataTransform(sheet: XSSFSheet): List[LinAlbaran] = {
    var is_return: Boolean = false
    var receipt_no: String = ""
    var vendor_no: String = ""
    var item_no: String = ""
    var quantity: Double = 0
    var unit_cost: Double = 0
    var percent_discount: Double = 0
    var amount: Double = 0
    var vat_amount: Double = 0
    var receipt_date: String = ""
    var order_no: String = ""
    var ceco: String = ""
    var entry_type: String = ""
    var lineas = new ListBuffer[LinAlbaran]()

    // Iterar por cada fila
    val rowIterator = sheet.iterator
    while ( {
      rowIterator.hasNext
    }) {
      val row = rowIterator.next

      is_return = false
      receipt_no = ""
      vendor_no = ""
      item_no = ""
      quantity = 0
      unit_cost = 0
      percent_discount = 0
      amount = 0
      vat_amount = 0
      receipt_date = ""
      order_no = ""
      ceco = ""
      entry_type = ""

      // Por cada fila, itero todas las columnas
      val cellIterator = row.cellIterator
      while ( {
        cellIterator.hasNext
      }) {
        val cell = cellIterator.next

        if (row.getRowNum > 0) {  // Me salto la primera fila porque son títulos de columnas
          cell.getColumnIndex match {
            case 0 =>
              val strReturn = cell.getStringCellValue
              is_return = strReturn.toBoolean
            case 1 =>
              try {
                receipt_no = cell.getStringCellValue
              } catch {
                case _: Throwable => receipt_no = cell.getNumericCellValue.toString
              }
            case 2 =>
              try {
                vendor_no = cell.getStringCellValue
              } catch {
                case _: Throwable => vendor_no = cell.getNumericCellValue.toString
              }
            case 3 =>
              try {
                item_no = cell.getStringCellValue
              } catch {
                case _: Throwable => item_no = cell.getNumericCellValue.toString
              }
            case 4 => quantity = cell.getNumericCellValue
            case 5 => unit_cost = cell.getNumericCellValue
            case 6 => percent_discount = cell.getNumericCellValue
            case 7 => amount = cell.getNumericCellValue
            case 8 => vat_amount = cell.getNumericCellValue
            case 9 =>
              val sdf = new SimpleDateFormat("yyyy-MM-dd")
              receipt_date = sdf.format(cell.getDateCellValue)

              //val sdf2 = new SimpleDateFormat("yyyy-MM-dd")
              //println(sdf2.format(cell.getDateCellValue))
            case 10 => order_no = cell.getStringCellValue
            case 11 => ceco = cell.getStringCellValue
            case 12 =>
              entry_type = cell.getStringCellValue

              receipt_no = receipt_no.replace("'", "")
              vendor_no = vendor_no.replace("'", "")
              item_no = item_no.replace("'", "")
              order_no = order_no.replace("'", "")
              ceco = ceco.replace("'", "")

              val lin = LinAlbaran(is_return, receipt_no, vendor_no, item_no, quantity,
                unit_cost, percent_discount, amount, vat_amount, receipt_date, order_no,
                ceco, entry_type)

              lineas += lin
          }
        }
      }
    }

    val miLista: List[LinAlbaran] = lineas.toList
    miLista
  }

  def postData(pLinea: LinAlbaran, endpoint: String, username: String, password: String): Unit = {
    val credentialsProvider = new BasicCredentialsProvider()
    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password))

    val lin_json = new Gson().toJson(pLinea)

    val post = new HttpPost(endpoint)

    post.setHeader("Content-type", "application/json")
    post.setEntity(new StringEntity(lin_json))

    val client = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build()
    val response = client.execute(post)

    println(response.getStatusLine.getStatusCode)
    println(response.getStatusLine.getReasonPhrase)
  }
}
