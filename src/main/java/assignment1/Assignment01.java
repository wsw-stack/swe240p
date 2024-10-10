package assignment1;

import java.util.Objects;

public class Assignment01 {
    public static void main(String[] args) {
        BankOfOC bankOfOC = new BankOfOC();
        User user1 = new User("Jean", "1000 1st st", "100", 1000);
        User user2 = new User("Tim", "1000 2nd avenue", "300", 1500);
        User user3 = new User("Tom", "1000 Irvine blvd", "450", 100);
        User user4 = new User("Jack", "1000 2nd avenue", "500", 10000);
        User user5 = new User("Jane", "567 coast dr", "666", 3400);
        User user6 = new User("Jane", "567 coast dr", "666", 12000);
        // LinkedList setup and addUser() testing
        bankOfOC.addUser(user1);
        bankOfOC.addUser(user2);
        bankOfOC.addUser(user3);
        bankOfOC.addUser(user4);
        bankOfOC.print(); // id from 1-4

        bankOfOC.deleteUser(2);
        bankOfOC.print(); // id: 1 3 4

        bankOfOC.deleteUser(5); // id=5 does not exist
        bankOfOC.addUser(user5); // it should have id=2
        bankOfOC.print(); // user5 is inserted at the 2nd position

        bankOfOC.payUserToUser(1, 6, 200); // id=6 does not exist, so it should raise an error message
        bankOfOC.payUserToUser(1, 3, 2000); // this user is too poor to pay $2000!
        bankOfOC.payUserToUser(2, 1, 1000);
        bankOfOC.print(); // notice the change in balance, Jean now has $2000 balance

        System.out.println(bankOfOC.getMedianID()); // 1 2 3 4, should return 2.5
        bankOfOC.addUser(user6);
        System.out.println(bankOfOC.getMedianID()); // 1 2 3 4 5, should return 3.0

        // now user5 and user6 have the same owner
        bankOfOC.mergeAccounts(1, 2); // merge id=1 and id=2 should not be allowed
        bankOfOC.mergeAccounts(2, 5); // they are both owned by Jane
        bankOfOC.print(); // expected balance of Jane: 12000 + 3400 - 1000 = 14400 (because Jane sent $1000 to Jean)

        // delete 2 users with id=2 and id=3, to prepare for merging, now {1 4}
        bankOfOC.deleteUser(2);
        bankOfOC.deleteUser(3);
        bankOfOC.print();
        // set up another bank
        User user7 = new User("Paul", "1020 5th avenue", "999", 500);
        User user8 = new User("Jenny", "1000 Dayton way", "450", 4500);
        User user9 = new User("Lucy", "1000 3rd st", "500", 1000);
        User user10 = new User("Katie", "15 station rd", "666", 8800);
        User user11 = new User("Anna", "580 campus dr", "666", 50000);
        BankOfOC bank2 = new BankOfOC();
        bank2.addUser(user7);
        bank2.addUser(user8);
        bank2.addUser(user9);
        bank2.addUser(user10);
        bank2.addUser(user11);
        // delete id=1 and id=5, now {2 3 4}
        bank2.deleteUser(1);
        bank2.deleteUser(5);
        bank2.print();

        // merge them together
        bankOfOC.mergeBanks(bank2); // expected {1 2 3 4 5} ({1 2 3 4 4} -> {1 2 3 4 5})
        bankOfOC.print();
    }
}

class User{
    private int id;
    private String name;
    private String address;
    private String ssn;
    private int balance;
    private User next;

    public User(String name, String address, String ssn, int balance) {
        this.name = name;
        this.address = address;
        this.ssn = ssn;
        this.balance = balance;
        this.next = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getNext() {
        return next;
    }

    public void setNext(User next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    // to determine if the accounts are owned by the same person
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(address, user.address) && Objects.equals(ssn, user.ssn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, ssn);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", ssn='" + ssn + '\'' +
                ", balance=" + balance +
                '}';
    }
}

class BankOfOC {
    private User head; // contains a head node (with no actual information stored inside)

    public BankOfOC() {
        this.head = new User(null, null, null, 0); // head node
        this.head.setId(0);
    }

    public void addUser(User user){
        User prev = head;
        User cur = head.getNext();
        while(cur != null && prev.getId() + 1 == cur.getId()){ // cur.id >= prev.id+2 means there is an empty slot for inserting
            prev = prev.getNext();
            cur = cur.getNext();
        }
        // whether, inserting at the end or inserting in the middle, it equals the previous id+1
        prev.setNext(user);
        user.setId(prev.getId()+1);
        user.setNext(cur);
    }

    public void deleteUser(int id){
        User prev = head;
        User cur = head.getNext();
        boolean isExist = false; // a variable to show if it exists
        while(cur != null){
            if(cur.getId() == id){
                isExist = true; // we've found the target
                prev.setNext(cur.getNext()); // change the next pointer of the previous node
                break; // end loop to save time, as all ids are unique
            }
            prev = cur;
            cur = cur.getNext();
        }
        // if not found, print the notification message
        if(!isExist) System.out.println("id={" + id + "} does not exist, delete failed");
    }

    public void payUserToUser(int id1, int id2, int amount){
        User user1 = head;
        User user2 = head;
        while(user1 != null){
            if(user1.getId() == id1) break;
            user1 = user1.getNext();
        }
        while(user2 != null){
            if(user2.getId() == id2) break;
            user2 = user2.getNext();
        }
        // check if both of them exist
        if(user1 == null){
            System.out.println("The sender account does not exist, please try again");
            return;
        }
        if(user2 == null){
            System.out.println("The receiver account does not exist, please try again");
            return;
        }
        // you must have enough balance to make a payment
        if(user1.getBalance() < amount){
            System.out.println("You must have enough balance to make this payment!");
            return;
        }
        // edit the balance
        user1.setBalance(user1.getBalance() - amount);
        user2.setBalance(user2.getBalance() + amount);
    }

    public double getMedianID(){
        User fast = head;
        User slow = head;

        while(fast != null && fast.getNext() != null){
            fast = fast.getNext().getNext();
            slow = slow.getNext();
        }
        // after the loop, the slow pointer stops at the middle, based on the number of nodes (even or odd), return the result
        if(fast == null){ // in this case, the number of nodes is odd
            return slow.getId();
        } else{ // even number of nodes
            return (double)(slow.getId() + slow.getNext().getId())/2.0;
        }
    }

    public void mergeAccounts(int id1, int id2){
        if(id2 == id1){
            System.out.println("You cannot merge same accounts!");
            return;
        }
        int smallerId = Math.min(id1, id2);
        int greaterId = Math.max(id1, id2);
        User user1 = head; // user1 will be pointing at the smaller id
        User user2 = head; // user2 will be pointing at the greater id
        while(user1 != null){
            if(user1.getId() == smallerId) break;
            user1 = user1.getNext();
        }
        while(user2 != null){
            if(user2.getId() == greaterId) break;
            user2 = user2.getNext();
        }
        // check if both of them exist
        if(user1 == null){
            System.out.println("The sender account does not exist, please try again");
            return;
        }
        if(user2 == null){
            System.out.println("The receiver account does not exist, please try again");
            return;
        }
        if(!user1.equals(user2)){ // check if they are owned by the same person
            System.out.println("They are not owned by the same person");
            return;
        }
        // change the current balance of user1 and remove user2
        user1.setBalance(user1.getBalance() + user2.getBalance());
        deleteUser(user2.getId());
    }

    public void mergeBanks(BankOfOC bank){
        User p1 = this.head;
        User p2 = bank.head.getNext();
        while(p2 != null){
            User temp;
            if(p1.getNext() == null){ // should insert p2 at the end
                temp = p2;
                p2 = p2.getNext();
                p1.setNext(temp);
                temp.setNext(null);
                p1 = p1.getNext();
            }
            // p2 can find a place to insert
            else if(p1.getId() <= p2.getId() && p2.getId() <= p1.getNext().getId()){
                temp = p2;
                p2 = p2.getNext();
                temp.setNext(p1.getNext());
                p1.setNext(temp);
            }
            else{ // p2 cannot be inserted after p1 (as its id value is too large)
                p1 = p1.getNext();
            }
        }
        // deal with duplicates
        User temp = this.head;
        while(temp.getNext().getNext() != null){
            // if the next two nodes have same id value (and same id value may appear only twice)
            if(temp.getNext().getId() == temp.getNext().getNext().getId()){
                User user = temp.getNext();
                temp.setNext(temp.getNext().getNext()); // change pointer of current node (to remove the next one)
                addUser(user); // remove it and insert it again for a unique ID
            }
            temp = temp.getNext();
        }
    }

    // print the whole linked list
    public void print(){
        User temp = head.getNext();
        System.out.println("The content of the list:");
        while (temp != null){
            System.out.println(temp);
            temp = temp.getNext();
        }
        System.out.println();
    }
}
