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

    def getByType(valTypeName: String): Option[List[String]] = {
        val opt = values.ValType(valTypeName)
        if opt.isEmpty then return None
        val namesList = scala.collection.mutable.ListBuffer[String]()
        val targetValType = opt.get
        for (name, dbVal) <- data
            if dbVal.isType(targetValType)
        do
            namesList += name
        Some(namesList.toList)
    }

    def getFields(name: String): Option[List[values.DBVal]] = {
        val opt = data.get(name)
        if opt.isEmpty then return None
        val dbVal = opt.get
        if dbVal.isStructured() then
            val dbStructVal = dbVal.asInstanceOf[values.DBStructVal]
            Option(dbStructVal.fields)
        else 
            None
    }
}