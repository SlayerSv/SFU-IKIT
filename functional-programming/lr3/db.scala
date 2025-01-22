package database

package values {

    enum ValType(val typeName: String) {
        case Int extends ValType("Int")
        case Float extends ValType("Float")
        case String extends ValType("String")
        case Struct extends ValType("Struct")
    }

    object ValType {
        def apply(name: String): Option[ValType] = {
            values.find(_.typeName == name)
        }
    }

    class DBVal(val name: String, val valType: ValType) {

        def isStructured(): Boolean = {
            isType(ValType.Struct)
        }

        def isType(comparedValType: ValType): Boolean = {
            valType == comparedValType
        }
    }

    class DBStructVal(name: String, val fields: List[DBVal] = List[DBVal]())
        extends DBVal(name, ValType.Struct) {

    }
}

class DB(data: Map[String, values.DBVal] = Map[String, values.DBVal]()) {

    def getType(name: String): Option[String] = {
        val opt = data.get(name)
        if opt.isDefined then
            Some(opt.get.valType.typeName)
        else
            None
    }

    def getByType(valTypeName: String): Optional[List[String]] = {
        getByTypes(List(valTypeName))
    }

    def getByTypes(typeNames: List[String]): Optional[List[String]] = {
        val namesList = scala.collection.mutable.ListBuffer[String]()
        val valTypes = scala.collection.mutable.Set[values.ValType]()
        for typeName <- typeNames
        do
            val opt = values.ValType(typeName)
            if opt.isDefined then
                valTypes += opt.get

        if valTypes.isEmpty then
            return None

        for (name, dbVal) <- data
        do
            for valType <- valTypes
            if dbVal.isType(valType)
            do
                namesList += name

        if namesList.isEmpty then
            None
        else
            Some(namesList.toList)
    }

    def getFields(name: String): Option[List[(String, String)]] = {
        val opt = data.get(name)
        if opt.isEmpty then
            return None
        
        val dbVal = opt.get
        if !dbVal.isStructured() then
            return None
        
        val dbStructVal = dbVal.asInstanceOf[values.DBStructVal]
        val fieldsList = scala.collection.mutable.ListBuffer[(String,String)]()
        for field <- dbStructVal.fields
        do
            fieldsList += (field.name, field.valType.typeName)

        Some(fieldsList.toList)
    }

    def isStructed(name: String): Optional[Boolean] = {
        val opt = data.get(name)
        if opt.isEmpty then
            return None

        Some(opt.get.isStructured())
    }
}

val dataD = Map[String, values.DBVal](
    ("id", values.DBVal("id", values.ValType.Int)),
    ("name", values.DBVal("name", values.ValType.String)),
    ("price", values.DBVal("price", values.ValType.Float)),
    ("address", values.DBStructVal("address", List(
        values.DBVal("house", values.ValType.Int),
        values.DBVal("city", values.ValType.String),
        values.DBVal("street", values.ValType.String)))),
    ("person", values.DBStructVal("person", List(
        values.DBVal("first name", values.ValType.String),
        values.DBVal("last name", values.ValType.String),
        values.DBVal("age", values.ValType.Int)))),
    ("weight", values.DBVal("weight", values.ValType.Float)),
    ("text", values.DBVal("text", values.ValType.String)),
    ("amount", values.DBVal("amount", values.ValType.Int)),
)