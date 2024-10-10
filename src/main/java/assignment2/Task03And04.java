package assignment2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Task03And04 {
    public static void main(String[] args) {
        Queue<Integer> queue = new Queue<>();
        try {
            int value = queue.poll();
            System.out.println("Head of queue: " + value);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } // expected: error
        queue.enqueue(1);
        queue.enqueue(3);
        try {
            int value = queue.poll();
            System.out.println("Head of queue: " + value);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } // expected: 1
        try {
            int value = queue.dequeue(); // expected: 1
            System.out.println("Head of queue: " + value);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } // expected: 1
        try {
            int value = queue.poll();
            System.out.println("Head of queue: " + value);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } // expected: 3
    }

    @Test
    public void testStack() {
        StackWithTwoQs<Object> stackWithTwoQs = new StackWithTwoQs<>();
        try {
            System.out.println(stackWithTwoQs.pop());
        } catch (IllegalStateException e){
            System.out.println("Error: " + e.getMessage()); // expected: Error
        }
        stackWithTwoQs.push(1);
        stackWithTwoQs.push(2);
        stackWithTwoQs.push(3);
        stackWithTwoQs.push(4);
        try {
            System.out.println(stackWithTwoQs.pop());
        } catch (IllegalStateException e){
            System.out.println("Error: " + e.getMessage()); // expected: 4
        }
        try {
            System.out.println(stackWithTwoQs.peek());
        } catch (IllegalStateException e){
            System.out.println("Error: " + e.getMessage()); // expected: 3
        }
        System.out.println(stackWithTwoQs.size()); // expected: 3
    }
}

class Queue<T> {
    List<T> elements;

    public Queue() {
        this.elements = new ArrayList<>();
    }

    public void enqueue(T newElem) {
        this.elements.add(newElem);
    }

    public T dequeue() {
        if(this.elements.isEmpty()) {
            throw new IllegalStateException("Queue is empty!");
        }
        return this.elements.remove(0); // remove the element at the head
    }

    public T poll() {
        if(this.elements.isEmpty()) {
            throw new IllegalStateException("Queue is empty!");
        }
        return this.elements.get(0); // get the head element
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int size() {return elements.size();}
}

// implementing a stack with 2 queues

class StackWithTwoQs<T>{

    Queue<T> queueOne; // your implemented Queue class

    Queue<T> queueTwo; // your implemented Queue class

    // all stack methods
    public StackWithTwoQs(){
        this.queueOne = new Queue<>(); // contains all elements except the queue tail
        this.queueTwo = new Queue<>(); // works like a helper,and contain at most one element (queue tail)
    }

    public void push(T x) {//pushes x in the stack.
        if(queueTwo.isEmpty()) queueTwo.enqueue(x);
        else{
            queueOne.enqueue(queueTwo.dequeue());
            queueTwo.enqueue(x); //
        }
    }

    public T pop(){ //removes the latest element from the stack and returns it.
        if(size() == 0) {
            throw new IllegalStateException("Stack is empty!");
        }
        T result = queueTwo.dequeue();
        while(queueOne.size() > 1) {
            queueTwo.enqueue(queueOne.dequeue());
        }
        // change queueOne and queueTwo
        Queue<T> temp = queueOne;
        queueOne = queueTwo;
        queueTwo = temp;
        return result;
    }

    public T peek(){ //returns the latest element from the stack without removing it
        if(size() == 0) {
            throw new IllegalStateException("Stack is empty!");
        }
        return queueTwo.poll();
    }

    public int size(){ //returns the size of the stack.
        return queueOne.size() + queueTwo.size();
    }

}
