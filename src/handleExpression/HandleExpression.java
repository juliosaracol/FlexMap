package handleExpression;

/**
 *
 * @author Dani Rosado
 */
public class HandleExpression {

     public static boolean checkParentheses(String s) {

        int contParentheses = 0;

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                contParentheses++;
            } else {
                if (s.charAt(i) == ')') {
                    contParentheses--;
                }
            }
        }

        if (contParentheses == 0) {
            return true;
        } else {
            return false;
        }
    }
     
    public static boolean checkParenthesesBeginAndEnd(String s) {

        int contParentheses = 0;

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                contParentheses++;
            } else {
                if (s.charAt(i) == ')') {
                    contParentheses--;
                }
            }

            if (contParentheses == 0) {
                if (i == s.length() - 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;

    }

   public static String deletSpace(String expression) {

        String s = "";
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) != ' ') {
                s += expression.charAt(i);
            }
        }

        return s;
    }
}
