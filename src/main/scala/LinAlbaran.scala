import com.google.gson.Gson
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.{BasicCredentialsProvider, HttpClientBuilder}
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.entity.StringEntity
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

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

object HttpJsonPostTest extends App {
  lee_excel()

  def post_lin_albaran(pLinea: LinAlbaran): Unit = {
    val username = "admin"
    val password = "z5rMIhW5J2H74LgtvGoPsicz/A02avHxnmLGMrbUu3Y="

    val credentialsProvider = new BasicCredentialsProvider()
    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password))

    val lin_json = new Gson().toJson(pLinea)

    val post = new HttpPost("http://bc3652020wave1:7048/BC/ODataV4/Company('Cronus%20Copia')/ReceiptLine")

    post.setHeader("Content-type", "application/json")
    post.setEntity(new StringEntity(lin_json))

    val client = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build()
    val response = client.execute(post)

    println(response.getStatusLine.getStatusCode)
    println(response.getStatusLine.getReasonPhrase)
  }

  def lee_excel(): Unit = {
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

    val file = new File("Example.xlsx")

    // Abro el libro excel
    val workbook = new XSSFWorkbook(file)

    // Obtengo primera hoja
    val sheet = workbook.getSheetAt(0)

    // Iterar por cada fila
    val rowIterator = sheet.iterator
    while ( {
      rowIterator.hasNext
    }) {
      val row = rowIterator.next
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
              case 1 => receipt_no = cell.getStringCellValue
              case 2 => vendor_no = cell.getStringCellValue
              case 3 => item_no = cell.getStringCellValue
              case 4 => quantity = cell.getNumericCellValue
              case 5 =>
                val strUnit_cost = cell.getStringCellValue
                unit_cost = strUnit_cost.toDouble
              case 6 => percent_discount = cell.getNumericCellValue
              case 7 =>
                val strAmount = cell.getStringCellValue
                amount = strAmount.toDouble
              case 8 =>
                val strVat_amount = cell.getStringCellValue
                vat_amount = strVat_amount.toDouble
              case 9 => receipt_date = cell.getStringCellValue
              case 10 => order_no = cell.getStringCellValue
              case 11 => ceco = cell.getStringCellValue
              case 12 =>
                entry_type = cell.getStringCellValue

                val lin = LinAlbaran(is_return, receipt_no, vendor_no, item_no, quantity,
                  unit_cost, percent_discount, amount, vat_amount, receipt_date, order_no,
                  ceco, entry_type)

                post_lin_albaran(lin)
            }
          }
        }
      }
  }
}

