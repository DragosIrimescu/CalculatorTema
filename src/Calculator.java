import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.*;

public class Calculator extends JFrame {

    JButton digits[] = {
            new JButton(" 0 "),
            new JButton(" 1 "),
            new JButton(" 2 "),
            new JButton(" 3 "),
            new JButton(" 4 "),
            new JButton(" 5 "),
            new JButton(" 6 "),
            new JButton(" 7 "),
            new JButton(" 8 "),
            new JButton(" 9 ")
    };

    JButton operators[] = {
            new JButton(" + "),
            new JButton(" - "),
            new JButton(" * "),
            new JButton(" / "),
            new JButton(" = "),
            new JButton(" C "),
            new JButton(" ( "),
            new JButton(" ) ")
    };

    String oper_values[] = {"+", "-", "*", "/", "=", ""};
    String expression = "";
    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args) {

        Calculator calculator = new Calculator();
        calculator.setSize(280, 280);
        calculator.setTitle(" Java-Calc, PP Lab1 ");
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator() {

        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (int i = 0; i < 10; i++) {
            buttonpanel.add(digits[i]);
        }

        for (int i = 0; i < 8; i++) {
            buttonpanel.add(operators[i]);
        }

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            digits[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    expression += Integer.toString(finalI);
                    area.append(Integer.toString(finalI));
                }
            });
        }

        for (int i = 0; i < 8; i++) {
            int finalI = i;
            operators[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (finalI == 4) {
                        evaluateExpression();
                    } else if (finalI == 5) {
                        area.setText("");
                        expression = "";
                    } else if (finalI == 6) {
                        expression += "(";
                        area.append("(");
                    } else if (finalI == 7) {
                        expression += ")";
                        area.append(")");
                    } else {
                        expression += oper_values[finalI];
                        area.append(oper_values[finalI]);
                    }
                }
            });
        }
    }

    private void evaluateExpression() {
        try {
            String result = eval_polska(polska(expression));
            area.append(" = " + result);
            expression = result;
        } catch (Exception e) {
            area.setText(" !!!Probleme!!! ");
            expression = "";
        }
    }

    private String polska(String expression) {
        Stack<Character> stack = new Stack<>();
        StringBuilder result = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c)) {
                result.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(' ').append(stack.pop());
                }
                stack.pop(); // Discard '('
            } else {
                // Operator
                while (!stack.isEmpty() && operatori_ordine(stack.peek()) >= operatori_ordine(c)) {
                    result.append(' ').append(stack.pop());
                }
                result.append(' ');
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            result.append(' ').append(stack.pop());
        }

        return result.toString().trim();
    }

    private String eval_polska(String Expression) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = Expression.split(" ");

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            if (Character.isDigit(token.charAt(0))) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        stack.push(a / b);
                        break;
                }
            }
        }

        return stack.pop().toString();
    }

    private int operatori_ordine(char operator) {
        switch (operator)
        {

            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
}