//> using scala 3.6.2
import scala.math.{pow, log}

val MAX_STEPS = 20
val MAX_ABS_VALUE = 1
val EXIT_ERROR_CODE = 1

@main
def main(start: Double, end: Double, step: Double, e: Double): Unit = {
    if start.abs >= MAX_ABS_VALUE || end.abs >= MAX_ABS_VALUE then
        System.err.println(s"ERROR: absolute values of start and end must be less than $MAX_ABS_VALUE.")
        System.exit(EXIT_ERROR_CODE)
    val steps = (end - start) / step
    if steps < 0 then
        System.err.println(s"ERROR: negative steps count. Check signs of start, end and step values")
        System.exit(EXIT_ERROR_CODE)
    if steps.round.toFloat != steps.toFloat && start != end then
        System.err.println(s"ERROR: cannot reach $end from $start with step $step")
        System.exit(EXIT_ERROR_CODE)
    if steps > MAX_STEPS then
        System.err.println(s"ERROR: max number of allowed steps is $MAX_STEPS. Required: ${steps.round}")
        System.exit(EXIT_ERROR_CODE)
    println(s"   x   |        f(x)        |      Taylor(x)     | Taylor Iterations")
    println(s"       |                    |                    |")
    solve(start, end, step, e)
}

def solve(currVal: Double, endVal: Double, step: Double, e: Double): Unit = {
    val (taylorRes, iterations) = calc(currVal, 0, 0, e)
    val fRes = log((1 + currVal) / (1 - currVal))
    val fResString = f"${if fRes >= 0 then " " else ""}%s$fRes%.15f"
    val taylorResString = f"${if taylorRes >= 0 then " " else ""}$taylorRes%.15f"
    val currValString = f"${if currVal >= 0 then " " else ""}%s$currVal%.2f"
    println(s" $currValString | $fResString | $taylorResString | $iterations")
    if currVal.toFloat == endVal.toFloat then
        return
    solve(currVal + step, endVal, step, e)
}

def calc(x: Double, prevVal: Double, iteration: Int, e: Double): (Double, Int) = {
    val n = 2 * iteration + 1
    val curr = pow(x, n) / n * 2 + prevVal
    if (curr - prevVal).abs < e.abs then
        (curr, iteration + 1)
    else
        calc(x, curr, iteration + 1, e)
}