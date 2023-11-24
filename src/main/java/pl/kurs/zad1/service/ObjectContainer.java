package pl.kurs.zad1.service;

import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectContainer<T extends Serializable> {

    private Node<T> head;
    private Predicate<T> condition;

    private static class Node<T> implements Serializable {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public ObjectContainer() {
        this.condition = obj -> true;
    }

    public ObjectContainer(Predicate<T> condition) {
        this.condition = condition;
    }

    public boolean add(T object) {
        if (!condition.test(object)) {
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

    public void storeToFile(String fileName) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
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
        ObjectContainer<U> container = new ObjectContainer<>(obj -> true);
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            container.head = (Node) inputStream.readObject();
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




    /*public static <T> ObjectContainer<T> fromFile(String fileName) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String commentAttribute = (String) Files.getAttribute(Paths.get(fileName), "comment", LinkOption.NOFOLLOW_LINKS);*/
//        Class<?> clazz = Class.forName(commentAttribute);

//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
//            ObjectContainer<Class> container  = ois.readObject();
//        }
//        return container;

      /*  Class aClass = Class.forName(commentAttribute);
        //TypeToken<T> typeToken = new TypeToken<T>(aClass.getClass()) {};
        //this.type = (Class<T>) typeToken.getRawType();
        //Constructor[] constructors = aClass.getConstructors();
        Field[] declaredFields = aClass.getDeclaredFields();
        Class[] fieldTypesArray = new Class[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            fieldTypesArray[i] = declaredFields[i].getClass();

        }
        Constructor constructor = aClass.getConstructor(fieldTypesArray);

//        MyObject myObject = (MyObject)
//                constructor.newInstance("constructor-arg1");
        ObjectContainer<T> objectContainer = new ObjectContainer<>();

        try (
                BufferedReader br = new BufferedReader(new FileReader(fileName))
        ) {
            String line = null;
            while ((line = br.readLine()) != null) {
                Object[] args = line.split(";");
                objectContainer.add((T) constructor.newInstance(args));
            }

        }
        return objectContainer;
    }

*/
//*/
//    public static <T> ObjectContainer<T> fromFile(String fileName, Function<String, T> converter) throws IOException, ClassNotFoundException {
//        String commentAttribute = (String) Files.getAttribute(Paths.get(fileName), "comment", LinkOption.NOFOLLOW_LINKS);
//        Class<?> clazz = Class.forName(commentAttribute);
//        ObjectContainer<> container = new ObjectContainer<>(obj -> true);
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                T obj = converter.apply(line);
//                container.add(obj);
//            }
//        }
//        return container;
//    }



