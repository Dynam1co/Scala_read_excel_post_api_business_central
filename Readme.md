# Read Excel file and POST data to Microsoft Dynamics 365 Business Central

This Scala program reads records from an excel file and inserts them into Business Central via POST using the standard API.

Both the excel file data and the API username and password are for testing.

## How does it work
A **case class** has been created with the necessary fields to generate a json string and send it via POST to Business Central.

```scala
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
```