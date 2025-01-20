//> using scala 3.6.2

val EXIT_ERROR_CODE = 1

@main
def main(path: String) = {
    try {
        val bufferedSource = scala.io.Source.fromFile(path)
        for line <- bufferedSource.getLines()
        do 
            println(line.split("\\s+").maxBy(_.size).size)
        bufferedSource.close()
    } catch {
        case e: java.io.FileNotFoundException => {
            System.err.println("ERROR: file does not exist.")
            System.exit(EXIT_ERROR_CODE)
        }
        case _ => {
            System.err.println("ERROR: unexpected error.")
            System.exit(EXIT_ERROR_CODE)
        }
    }
}