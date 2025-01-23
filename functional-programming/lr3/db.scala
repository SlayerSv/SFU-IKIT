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

// Использование Map позволяет проверять наличие и получать данные за О(1),
// но ведет к использованию чуть большей памяти.
// Таким образом получается аналог базы данных с хеш индексом по полю name.
class DB(data: Map[String, values.DBVal] = dataD) {

    def getType(name: String): Option[String] = {
        val opt = data.get(name)
        if opt.isDefined then
            Some(opt.get.valType.typeName)
        else
            None
    }

    def getByType(valTypeName: String): Option[List[String]] = {
        getByTypes(List(valTypeName))
    }

    def getByTypes(typeNames: List[String]): Option[List[String]] = {
        val validTypeNames = typeNames.filter(values.ValType(_).isDefined)
        if validTypeNames.isEmpty then
            return None
        else
            Some(data.
                filter((name, dbVal) => validTypeNames.contains(dbVal.valType.typeName)).
                keys.
                toList
        )
    }

    def getFields(name: String): Option[List[(String, String)]] = {
        val opt = data.get(name)
        if opt.isEmpty then
            return None
        
        val dbVal = opt.get
        if !dbVal.isStructured() then
            return None
        
        val dbStructVal = dbVal.asInstanceOf[values.DBStructVal]
        Some(dbStructVal.fields.map(field => (field.name, field.valType.typeName)))
    }

    def isStructured(name: String): Option[Boolean] = {
        val opt = data.get(name)
        if opt.isEmpty then
            None
        else
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