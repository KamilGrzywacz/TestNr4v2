package pl.kurs.zad1.app;

import pl.kurs.zad1.models.Person;
import pl.kurs.zad1.service.ObjectContainer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Runner {
    public static void main(String[] args) throws IOException {


        ObjectContainer<Person> peopleFromWarsaw = new ObjectContainer<>(p -> p.getCity().equals("Warsaw"));
        peopleFromWarsaw.add(new Person("Jan", "Warsaw", 30));
        peopleFromWarsaw.add(new Person("Weronika", "Warsaw", 20));
        peopleFromWarsaw.add(new Person("Waldek", "Monaco", 34));


        List<Person> females = peopleFromWarsaw.getWithFilter(p -> p.getName().endsWith("a"));
        System.out.println("Lista Females : " + females);

        peopleFromWarsaw.removeIf(p -> p.getAge() > 50);
        System.out.println("People from Warsaw : " + peopleFromWarsaw);


        peopleFromWarsaw.storeToFile("youngPeopleFromWarsaw.txt", p -> p.getAge() < 30, p -> p.getName() + ";" + p.getAge() + ";" + p.getCity());
        peopleFromWarsaw.storeToFile("warsawPeople.txt");
        ObjectContainer<Person> peopleFromWarsawFromFile = ObjectContainer.fromFile("warsawPeople.txt");
        System.out.println("peopleFromWarsawFromFile = " + peopleFromWarsawFromFile);

    }
}
