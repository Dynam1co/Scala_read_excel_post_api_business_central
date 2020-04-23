import java.io.FileInputStream
import java.util.Properties

import Utils.LinAlbaran

object Main extends App {
  val prop = new Properties()
  prop.load(new FileInputStream("src/main/resources/default.properties"))

  val user: String = prop.getProperty("api_user")
  val pass: String = prop.getProperty("api_password")
  val final_endpoint: String = prop.getProperty("api_endpoint_general") + prop.getProperty("api_endpoint_albaranes")

  val sheet = Utils.readExcel()

  val miLista: List[LinAlbaran] = Utils.dataTransform(sheet)

  for (linea <- miLista) Utils.postData(linea, final_endpoint, user, pass)
}

