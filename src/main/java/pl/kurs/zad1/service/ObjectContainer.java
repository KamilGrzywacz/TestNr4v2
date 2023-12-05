package pl.kurs.zad1.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectContainer<T extends Serializable> implements Serializable {

    private Node<T> head;
    private MyPredicate<T> serializedPredicate;

    public ObjectContainer() {

    }


    private static class Node<T> implements Serializable {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public ObjectContainer(Predicate<T> condition) {
        this.serializedPredicate = new MyPredicate<>();
    }

    public boolean add(T object) {
        if (object == null || !serializedPredicate.test(object)) {
            return false;
        }
        Node<T> newNode = new Node<>(object);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        return true;
    }


    public List<T> getWithFilter(Predicate<T> filter) {
        List<T> resultList = new ArrayList<>();
        Node<T> temp = head;
        while (temp != null) {
            if (filter.test(temp.data)) {
                resultList.add(temp.data);
            }
            temp = temp.next;
        }
        return resultList;
    }

    public void removeIf(Predicate<T> filter) {
        while (head != null && filter.test(head.data)) {
            head = head.next;
        }

        Node<T> current = head;
        while (current != null && current.next != null) {
            if (filter.test(current.next.data)) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }

    }

    //    public void storeToFile(String fileName) throws IOException {
//        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
//            outputStream.writeObject(serializedPredicate);
//            outputStream.writeObject(head);
//        }
//    }
    public void storeToFile(String fileName) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(serializedPredicate);
            outputStream.writeObject(head);
        }
    }

    public void storeToFile(String fileName, Predicate<T> filter, Function<T, String> formatter) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Node<T> temp = head;
            while (temp != null) {
                if (filter.test(temp.data)) {
                    writer.write(formatter.apply(temp.data) + "\n");
                }
                temp = temp.next;
            }
        }
    }

    public static <U extends Serializable> ObjectContainer<U> fromFile(String fileName) {
        ObjectContainer<U> container = new ObjectContainer<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            MyPredicate<U> predicate = (MyPredicate<U>) inputStream.readObject();
            container.serializedPredicate = predicate;
            container.head = (Node<U>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return container;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data.toString()).append("\n");
            current = current.next;
        }
        return sb.toString();
    }
}





