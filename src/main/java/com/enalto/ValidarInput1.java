package com.enalto;

import java.util.Scanner;
import java.util.function.Predicate;

public class ValidarInput1 {

    public static <T> T validateInput(Scanner scanner, String prompt, Predicate<T> validator) {
        T input;
        while (true) {
            System.out.print(prompt);
            try {
                input = (T) scanner.nextLine();
                if (validator.test(input)) {
                    return input;
                } else {
                    System.out.println("Entrada invalida. Por favor, tente novamente.");
                }
            } catch (ClassCastException e) {
                System.out.println("Formato de entrada invalido. Por favor, tente novamente.");
            }
        }
    }
}
