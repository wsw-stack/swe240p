package assignment4;

import org.junit.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class BST {
    Node root;
    public void insert(Node node) {
        if(root == null) {
            this.root = node;
        } else {
            root.insert(root, node);
        }
    }

    public void inOrderTraversal(String filePath) throws IOException {
        if(root == null) {
            System.out.println("Tree is empty!");
            return;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        root.inOrderTraversal(root, writer);
        writer.close();
    }

    public void preOrderTraversal(String filePath) throws IOException {
        if(root == null) {
            System.out.println("Tree is empty!");
            return;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        root.preOrderTraversal(root, writer);
        writer.close();
    }

    public void postOrderTraversal(String filePath) throws IOException {
        if(root == null) {
            System.out.println("Tree is empty!");
            return;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        root.postOrderTraversal(root, writer);
        writer.close();
    }

    public void levelOrderTraversal(String filePath) throws IOException {
        if(root == null) {
            System.out.println("Tree is empty!");
            return;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        root.levelOrderTraversal(root, writer);
        writer.close();
    }

    public void delete(String lastName) {
        root.deleteNode(root, lastName);
    }

    @Test
    public void test() throws IOException {
        String filename = "src/tree-input.txt";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] parts = line.split("\\s+"); // split the line into 3 parts
            char method = parts[0].charAt(0);
            String stuNum = parts[0].substring(1, 8); // 7-digit student number
            String lastName = parts[0].substring(8); // student last name
            String deptNum = parts[1].substring(0, 4); // department number
            String program = parts[1].substring(4); // program code
            int year = Integer.parseInt(parts[2]); // get the number of year

            if(method == 'I') {
                Student student = new Student(stuNum, lastName, deptNum, program, year);
                insert(new Node(student));
            } else {
                delete(lastName);
            }
        }
        String inOrderFile = "src/in_order.txt";
        String preOrderFile = "src/pre_order.txt";
        String postOrderFile = "src/post_order.txt";
        String levelOrderFile = "src/level_order.txt";
//        inOrderTraversal(inOrderFile); // should be a non-decreasing sequence
        System.out.println("====================");
        preOrderTraversal(preOrderFile);
        System.out.println("====================");
        postOrderTraversal(postOrderFile);
        System.out.println("====================");
        levelOrderTraversal(levelOrderFile);
        System.out.println("====================");
        delete("Tom"); // does not exist
        delete("Schafer");
        inOrderTraversal(inOrderFile);
    }
}

class Node {
    Student data;
    Node left;
    Node right;

    Node(Student data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public void insert(Node root, Node newNode) {
        if(root.data.compareTo(newNode.data) < 0) {
            if(root.right == null) {
                root.right = newNode;
            } else {
                insert(root.right, newNode);
            }
        } else {
            if(root.left == null) {
                root.left = newNode;
            } else {
                insert(root.left, newNode);
            }
        }
    }

    public void inOrderTraversal(Node node, BufferedWriter writer) throws IOException {
        if(node == null) {
            return;
        }
        inOrderTraversal(node.left, writer);
        writer.write(node.data.toString());
        writer.newLine();
        System.out.println(node.data);
        inOrderTraversal(node.right, writer);
    }

    public void preOrderTraversal(Node node, BufferedWriter writer) throws IOException {
        if(node == null) {
            return;
        }
        System.out.println(node.data);
        writer.write(node.data.toString());
        writer.newLine();
        preOrderTraversal(node.left, writer);
        preOrderTraversal(node.right, writer);
    }

    public void postOrderTraversal(Node node, BufferedWriter writer) throws IOException {
        if(node == null) {
            return;
        }
        postOrderTraversal(node.left, writer);
        postOrderTraversal(node.right, writer);
        System.out.println(node.data);
        writer.write(node.data.toString());
        writer.newLine();
    }

    public void levelOrderTraversal(Node node, BufferedWriter writer) throws IOException {
        Queue<Node> queue = new LinkedList<>();
        queue.offer(node);
        while(!queue.isEmpty()) {
            Node cur = queue.poll();
            System.out.println(cur.data);
            writer.write(cur.data.toString());
            writer.newLine();
            if(cur.left != null) {
                queue.offer(cur.left);
            }
            if(cur.right != null) {
                queue.offer(cur.right);
            }
        }
    }

    // search for a node and return the corresponding node and its parent
    public Node[] search(Node root, Node pre, String key) {
        if (root == null) {
            return new Node[] { null, pre };
        }
        if (root.data.lastName.compareTo(key) > 0) {
            return search(root.left, root, key);
        } else if (root.data.lastName.compareTo(key) < 0) {
            return search(root.right, root, key);
        } else {
            return new Node[] { root, pre };
        }
    }

    public Node deleteNode(Node root, String lastName) {
        Node[] group = search(root, null, lastName);
        Node cur = group[0];
        Node pre = group[1];
        if (cur == null) {
            System.out.println("Key does not exist!");
            return root; // key not in the tree
        }
        // removing a leaf node
        if (cur.left == null && cur.right == null) {
            if (pre != null) {
                if (pre.data.lastName.compareTo(cur.data.lastName) < 0) { // cur is the right child
                    pre.right = null;
                } else {
                    pre.left = null;
                }
                return root;
            } // root node is also a leaf node
            return null;
        } else { // not a leaf node
            // only has a left or right child
            if (cur.left == null || cur.right == null) {
                // if we are removing the root node
                if (pre == null) {
                    if (cur.left == null) {
                        return cur.right;
                    } else {
                        return cur.left;
                    }
                } else {
                    if (cur.left == null) {
                        if (pre.data.lastName.compareTo(cur.data.lastName) < 0) { // cur is pre's right child
                            pre.right = cur.right;
                        } else {
                            pre.left = cur.right; // cur is pre's left child
                        }
                    } else {
                        if (pre.data.lastName.compareTo(cur.data.lastName) < 0) { // cur is pre's right child
                            pre.right = cur.left;
                        } else {
                            pre.left = cur.left; // cur is pre's left child
                        }
                    }
                }
            } else { // removing a node with both left and right child
                // try to find the right-most child of its left sub-tree
                Node preTemp = cur.left;
                Node temp = preTemp.right;
                if (temp == null) { // left sub-tree has no right child
                    cur.data = preTemp.data;
                    cur.left = preTemp.left;
                    return root;
                }
                while (temp.right != null) {
                    preTemp = preTemp.right;
                    temp = temp.right;
                }
                cur.data = temp.data;
                preTemp.right = temp.left;
            }
        }
        return root;
    }
}

class Student implements Comparable<Student>{
    String stuNum;
    String lastName;
    String homeDept;
    String program;
    int year;

    public Student(String stuNum, String lastName, String homeDept, String program, int year) {
        this.stuNum = stuNum;
        this.lastName = lastName;
        this.homeDept = homeDept;
        this.program = program;
        this.year = year;
    }

    @Override
    public int compareTo(Student student) {
        return this.lastName.compareTo(student.lastName);
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuNum='" + stuNum + '\'' +
                ", lastName='" + lastName + '\'' +
                ", homeDept='" + homeDept + '\'' +
                ", program='" + program + '\'' +
                ", year=" + year +
                '}';
    }
}
