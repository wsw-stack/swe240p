package assignment2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Task01And02 {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        try {
            int value = stack.peek();
            System.out.println("Top of stack: " + value);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } // expected: error
        stack.push(1);
        stack.push(3);
        try {
            int value = stack.peek();
            System.out.println("Top of stack: " + value);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } // expected: 3
        try {
            int value = stack.pop(); // expected: 1
            System.out.println("Top of stack: " + value);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } // expected: 3
        try {
            int value = stack.peek();
            System.out.println("Top of stack: " + value);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } // expected: 1
    }

    @Test
    public void testCalculator01(){
        String expression = "10 + 20 * 2";
        System.out.println(calculator(expression));
    }

    @Test
    public void testCalculator02(){
        String expression = "10 + 20 * 2 - 3/1";
        System.out.println(calculator(expression));
    }

    @Test
    public void testCalculator03(){
        String expression = "10 + 20 * 2 / 4";
        System.out.println(calculator(expression));
    }

    @Test
    public void testCalculator04(){
        String expression = "10 + 20 / 0 - 6";
        System.out.println(calculator(expression));
    }

    @Test
    public void testCalculator05(){
        String expression = "lol10 + 20 * 2 / 4 - 3"; // will raise an error
        System.out.println(calculator(expression));
    }

    @Test
    public void testCalculator06(){
        String expression = "/10 + 20 * 2 / 4 - 3"; // will raise an error due to operators
        System.out.println(calculator(expression));
    }

    public double calculator(String expression) {
        // cannot contain characters except 0-9 and + - / *, also the last and beginning character cannot be an operator
        if(!expression.matches("[0-9][0-9+\\-*/\\s]+[0-9]")){
            throw new IllegalStateException("Illegal expression, please check");
        }
        Stack<Double> numStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        // shows if the last char is an operator
        boolean prevIsOperator = true; // initialized to true because the first char cannot be an operator
        // build up the two stacks
        for(char c: expression.toCharArray()) {
            if(c == ' ') continue; // skip white spaces
            // if operator appears twice in a row or the first char is an operator
            if(prevIsOperator && !Character.isDigit(c)) {
                throw new IllegalStateException("Illegal expression, operators cannot appear twice in a row");
            }
            // if previous digit is also a number or is the first char
            if(!prevIsOperator && Character.isDigit(c)) {
                double curTop = numStack.pop();
                numStack.push(curTop * 10 + c - '0'); // update the new top number, e.g 2 -> 23
            }
            // if the prev is an operator, add a new number
            else if(prevIsOperator && Character.isDigit(c)) {
                numStack.push((double)c - '0');
                prevIsOperator = false;
            }
            // if c is an operator
            else if(!Character.isDigit(c)){
                // if top is * or /, must use it first due to priority
                if(!operatorStack.isEmpty() && (operatorStack.peek() == '/' || operatorStack.peek() == '*')){
                    char operator = operatorStack.pop();
                    if(operator == '*') {
                        double base = numStack.pop();
                        double multiplier = numStack.pop();
                        numStack.push(base * multiplier);
                    }
                    else{
                        double divisor = numStack.pop();
                        double dividend = numStack.pop();
                        if(divisor == 0.0) {
                            throw new IllegalStateException("Divisor cannot be zero!");
                        } else{
                            numStack.push(dividend / divisor);
                        }
                    }
                }
                // push the new operator
                operatorStack.push(c);
                prevIsOperator = true;
            }
        }
        // calculate
        while(!operatorStack.isEmpty()) {
            char operator = operatorStack.pop();
            if(operator == '*') {
                double base = numStack.pop();
                double multiplier = numStack.pop();
                numStack.push(base * multiplier);
            } else if(operator == '/') {
                double divisor = numStack.pop();
                double dividend = numStack.pop();
                if(divisor == 0.0) {
                    throw new IllegalStateException("Divisor cannot be zero!");
                } else{
                    numStack.push(dividend / divisor);
                }
            } else if(operator == '+') {
                double a = numStack.pop();
                double b = numStack.pop();
                numStack.push(a + b);
            } else {
                double a = numStack.pop();
                double b = numStack.pop();
                numStack.push(b - a);
            }
        }
        return numStack.pop();
    }
}

//// a class for calculator task
//class Component {
//    String value;
//    boolean isNum; // is true when it is a number, false when it is an operator
//
//    public Component(String value, boolean isNum) {
//        this.value = value;
//        this.isNum = isNum;
//    }
//}

class Stack<T> {
    List<T> elements;

    public Stack() {
        this.elements = new ArrayList<>();
    }

    public void push(T newElem) {
        this.elements.add(newElem);
    }

    public T pop() {
        if(this.elements.isEmpty()) {
            throw new IllegalStateException("Stack is empty!");
        }
        int size = this.elements.size();
        return this.elements.remove(size-1); // remove the element with the largest index
    }

    public T peek() {
        if(this.elements.isEmpty()) {
            throw new IllegalStateException("Stack is empty!");
        }
        int size = this.elements.size();
        return this.elements.get(size-1); // remove the element with the largest index
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }
}
