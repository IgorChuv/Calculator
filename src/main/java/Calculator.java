import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class Calculator {
    public static void main(String[] args) {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        String input;

        try {
            while (true) {
                System.out.println("Введите выражение для расчёта:");
                input = buffer.readLine();
                if (input.equals("end")) {
                    break;
                }
                input = stringConversion(input);
                System.out.println("Результат: " + calculate(input));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Преобразовывает строку в обратную польскую нотацию
    private static String stringConversion(String input) throws Exception {
        StringBuilder stack = new StringBuilder("");
        StringBuilder out = new StringBuilder("");
        char inputChar;
        char temp;

        for (int i = 0; i < input.length(); i++) {
            inputChar = input.charAt(i);
            if (isOperator(inputChar)) {
                while (stack.length() > 0) {
                    temp = stack.substring(stack.length() - 1).charAt(0);
                    if (isOperator(temp) && (operationPriority(inputChar) <= operationPriority(temp))) {
                        out.append(" ").append(temp).append(" ");
                        stack.setLength(stack.length() - 1);
                    } else {
                        out.append(" ");
                        break;
                    }
                }
                out.append(" ");
                stack.append(inputChar);
            } else if ('(' == inputChar) {
                stack.append(inputChar);
            } else if (')' == inputChar) {
                temp = stack.substring(stack.length() - 1).charAt(0);
                while ('(' != temp) {
                    if (stack.length() < 1) {
                        throw new Exception("Неправильно указаны скобки");
                    }
                    out.append(" ").append(temp);
                    stack.setLength(stack.length() - 1);
                    temp = stack.substring(stack.length() - 1).charAt(0);
                }
                stack.setLength(stack.length() - 1);
            } else {
                out.append(inputChar);
            }
        }
        while (stack.length() > 0) {
            out.append(" ").append(stack.substring(stack.length() - 1));
            stack.setLength(stack.length() - 1);
        }
        return out.toString();
    }

    //Метод проверяющий является ли символ оператором
    private static boolean isOperator(char ch) {
        return switch (ch) {
            case '-', '+', '*', '/' -> true;
            default -> false;
        };
    }

    //Метод проверяющий приоритет входящего оператора
    private static int operationPriority(char operator) {
        return switch (operator) {
            case '*', '/' -> 2;
            default -> 1;
        };
    }

    //Расчёт выражения, записанного в обратной польской нотации
    private static double calculate(String input) throws Exception {
        double firstNum = 0;
        double secondNum = 0;
        String temp;
        Deque<Double> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(input);
        while (tokenizer.hasMoreTokens()) {
            try {
                temp = tokenizer.nextToken().trim();
                if (1 == temp.length() && isOperator(temp.charAt(0))) {
                    secondNum = stack.pop();
                    firstNum = stack.pop();
                    switch (temp.charAt(0)) {
                        case '+' -> firstNum += secondNum;
                        case '-' -> firstNum -= secondNum;
                        case '/' -> firstNum /= secondNum;
                        case '*' -> firstNum *= secondNum;
                        default -> throw new Exception("Недопустимая операция " + temp);
                    }
                    stack.push(firstNum);
                } else {
                    firstNum = Double.parseDouble(temp);
                    stack.push(firstNum);
                }
            } catch (Exception e) {
                throw new Exception("Недопустимый символ в выражении");
            }
        }
        return stack.pop();
    }
}