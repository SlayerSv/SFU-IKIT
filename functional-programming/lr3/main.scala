//> using scala 3.6.2

@main
def main() = {
    val db = DB()
    getByType("Int", db)
    getByType("Float", db)
    getByType("String", db)
    getByType("Struct", db)
    getByType("NonExisting", db)
    getByTypes(List("String", "Struct"), db)
    getByTypes(List("NonExisting1", "NonExisting2"), db)
    isStructured("name", db)
    isStructured("person", db)
    isStructured("nonExisting", db)
    getType("address", db)
    getType("amount", db)
    getType("nonExisting", db)
    getFields("person", db)
    getFields("address", db)
    getFields("weight", db)
    getFields("nonExisting", db)
}

def isStructured(varName: String, db: DB): Unit = {
    val opt = db.isStructured(varName)
    if opt.isEmpty then
        return println(s"Variable '$varName' does not exist in the database.")
    val isStruct = opt.get
    println(s"Variable '$varName' is${if isStruct then "" else " not"} structured.")
}

def getType(varName: String, db: DB): Unit = {
    val opt = db.getType(varName)
    if opt.isEmpty then
        return println(s"Variable '$varName' does not exist in the database.")
    println(s"Variable '$varName' is of type '${opt.get}'.")
}

def getByType(typeName: String, db: DB): Unit = {
    val opt = db.getByType(typeName)
    if opt.isEmpty then
        return println(s"Type '$typeName' does not exist in the database.")
    val varNames = opt.get
    if varNames.isEmpty then
        return println(s"There are no variables of type '$typeName' in the database.")
    println(s"Variables of type '$typeName' in the database are: ${varNames.
        map(varName => s"'$varName'").
        mkString(", ")}.")
}

def getByTypes(typeNames: List[String], db: DB): Unit = {
    val opt = db.getByTypes(typeNames)
    if opt.isEmpty then
        return println(s"Types ${typeNames.
            map(typeName => s"'$typeName'").
            mkString(", ")} do not exist in the database.")
    val varNames = opt.get
    if varNames.isEmpty then
        return println(s"There are no variables of types ${typeNames.
            map(typeName => s"'$typeName'").
            mkString(", ")} in the database.")
    println(s"Variables of types ${typeNames.
        map(typeName => s"'$typeName'").
        mkString(", ")} in the database are: '${varNames.
        map(varName => s"'$varName'").
        mkString(", ")}.")
}

def getFields(varName: String, db: DB): Unit = {
    val opt = db.getFields(varName)
    if opt.isEmpty then
        return println(s"Cannot get fields of '$varName' because it does not exist or is not structured.")
    println(s"Fields of '$varName' are: ${opt.get.
        map((name, typeName) => s"'$name'('$typeName')").
        mkString(", ")}.")
}