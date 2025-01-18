//> using scala 3.6.2
import scala.math.pow

val MAX_STEPS = 20
val MAX_ABS_VALUE = 1
val EXIT_ERROR_CODE = 1

@main
def calc(start: Double, end: Double, step: Double, e: Double): Unit = {
    if (start.abs >= MAX_ABS_VALUE || end.abs >= MAX_ABS_VALUE) {
        System.err.println(s"ERROR: absolute values of start and end must be less than $MAX_ABS_VALUE.")
        System.exit(EXIT_ERROR_CODE)
    }
    val steps = (end - start) / step
    if (steps < 0) {
        System.err.println(s"ERROR: negative steps count. Check signs of start, end and step values")
        System.exit(EXIT_ERROR_CODE)
    }
    if (steps.round.toFloat != steps.toFloat && start != end) {
        System.err.println(s"ERROR: cannot reach $end from $start with step $step")
        System.exit(EXIT_ERROR_CODE)
    }
    if (steps > MAX_STEPS) {
        System.err.println(s"ERROR: max number of allowed steps is $MAX_STEPS. Required: ${steps.round}")
        System.exit(EXIT_ERROR_CODE)
    }
    println(s"  x   |      Taylor(x)     | Taylor Iterations")
    println(s"      |                    |")
    solve(start, end, step, e)
}

def solve(currValue: Double, endValue: Double, step: Double, e: Double): Unit = {
    val (res, iterations) = calc(currValue, 0, 0, e)
    println(f"${if currValue >= 0 then " " else ""}%s$currValue%3.2f | ${if res >= 0 then " " else ""}$res%3.15f | $iterations%d")
    if (currValue.toFloat == endValue.toFloat) {
        return
    }
    solve(currValue + step, endValue, step, e)

}

def calc(x: Double, prevValue: Double, iteration: Int, e: Double): (Double, Int) = {
    val n = 2 * iteration + 1
    val curr = pow(x, n) / n * 2 + prevValue
    if ((curr - prevValue).abs < e.abs) {
        return (curr, iteration + 1)
    }
    return calc(x, curr, iteration + 1, e)
}