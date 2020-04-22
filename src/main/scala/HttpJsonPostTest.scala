import Utils.LinAlbaran

object HttpJsonPostTest extends App {
  val endpoint: String = "http://bc3652020wave1:7048/BC/ODataV4/Company('Cronus%20Copia')/ReceiptLine"
  val user: String = "admin"
  val pass: String = "z5rMIhW5J2H74LgtvGoPsicz/A02avHxnmLGMrbUu3Y="

  val sheet = Utils.readExcel()

  val miLista: List[LinAlbaran] = Utils.dataTransform(sheet)

  for (linea <- miLista) Utils.postData(linea, endpoint, user, pass)
}

