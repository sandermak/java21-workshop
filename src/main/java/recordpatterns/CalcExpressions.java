package recordpatterns;

public class CalcExpressions {
    static final CalcExpr BASIC_EXPR = new Add(new Constant(1), new Constant(2));
    static final CalcExpr TO_BE_SIMPLIFIED_EXPR1 = new Subtract(new Add(new Subtract(new Constant(1), new Constant(0)), new Add(new Constant(0), new Constant(2))), new Constant(0));
    static final CalcExpr TO_BE_SIMPLIFIED_EXPR2 = new Add(new Mul(new Constant(0), new Constant(2)), new Mul(new Constant(3), new Subtract(new Constant(4), new Constant(0))));

    public static void main(String... args) {
        int result = calculate(BASIC_EXPR);
        System.out.println("%s = %s".formatted(BASIC_EXPR, result));

        CalcExpr simplified1 = simplify(TO_BE_SIMPLIFIED_EXPR1);
        System.out.println("%s = %s = %s".formatted(TO_BE_SIMPLIFIED_EXPR1, simplified1, calculate(TO_BE_SIMPLIFIED_EXPR1)));

        CalcExpr simplified2 = simplify(TO_BE_SIMPLIFIED_EXPR2);
        System.out.println("%s = %s = %s".formatted(TO_BE_SIMPLIFIED_EXPR2, simplified2, calculate(TO_BE_SIMPLIFIED_EXPR2)));

    }

    // Should evaluate an expression and return the resulting value
    //
    // 1. Implement this method using switch and record patterns.
    // 2. Add a record `Pow(CalcExr expr, int exponent)`, and evaluate an expression with it. What happens?
    // 3. Can you get rid of the default? That will help address future implementations of `CalcExpr` like `Pow`!
    private static int calculate(CalcExpr basicExpr) {
        return 0;
    }

    // Simplify the expression provided. E.g. 2 + 0 becomes 2,
    // 3 * 0 becomes 0, and 4 - 0 becomes 4 (but note subtraction is
    // not commutative, so 0 - 4 is better left untouched!)
    // You can of course come up with more simplifications if you want.
    //
    // Implement using switch and (guarded, nested?) record patterns!
    private static CalcExpr simplify(CalcExpr toBeSimplifiedExpr) {
        return toBeSimplifiedExpr;
    }

    interface CalcExpr {
    }

    record Constant(int i) implements CalcExpr {
        @Override
        public String toString() {
            return "" + i;
        }
    }

    record Add(CalcExpr left, CalcExpr right) implements CalcExpr {
        @Override
        public String toString() {
            return "(%s + %s)".formatted(left, right);
        }
    }

    record Subtract(CalcExpr left, CalcExpr right) implements CalcExpr {
        @Override
        public String toString() {
            return "(%s - %s)".formatted(left, right);
        }
    }

    record Mul(CalcExpr left, CalcExpr right) implements CalcExpr {
        @Override
        public String toString() {
            return "(%s * %s)".formatted(left, right);
        }
    }

}
