package pl.kurs.zad2;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class  Runner {
    public static void main(String[] args) throws InvlaidPeselException {

        System.out.println("Podaj swoje imie:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Optional<String> optionalName = Optional.ofNullable(name);
        int lenght = optionalName.map(String::length)
                .orElse(0);
        System.out.println("Długośc imienia to: " + lenght);

        System.out.println("Podaj pesel");
        String pesel = scanner.nextLine();

        try {
            String birthDate = getBirthDateFromPesel(pesel);
            System.out.println("Twoja data urodzenia: " + birthDate);
        } catch (InvlaidPeselException e) {
            System.out.println("Niepoprawny pesel: " + e.getMessage());
        }
    }

    private static String getBirthDateFromPesel(String pesel) throws InvlaidPeselException {
        return Optional.ofNullable(pesel)
                .filter(x -> x.matches("\\d{11}"))
                .map(p -> {
                    String year = p.substring(0, 2);
                    String month = p.substring(2, 4);
                    String day = p.substring(4, 6);
                    return day + "/" + month + "/" + year;
                })
                .orElseThrow(() -> new InvlaidPeselException("Niepoprawny pesel."));
    }
}
