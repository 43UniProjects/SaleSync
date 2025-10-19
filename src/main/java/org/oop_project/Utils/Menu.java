package org.oop_project.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class Menu {

    private static final Scanner read = new Scanner(System.in);

    public static int displayMenu(String[] menuItems, boolean printVertical) {
        return displayMenu(Arrays.stream(menuItems).iterator(), printVertical, menuItems.length);
    }

    public static int displayMenu(ArrayList<String> menuItems, boolean printVertical) {
        return displayMenu(menuItems.iterator(), printVertical, menuItems.size());
    }

    public static int displayMenu(Iterator<String> menuIter, boolean printVertical, int menuItemCount) {
        int selectedOptionIndex = -1;
        System.out.println();
        if (printVertical) {
            printVertical(menuIter);
        } else {
            printHorizontal(menuIter);
        }
        while (selectedOptionIndex == -1) {
            selectedOptionIndex = getInput(menuItemCount);
        }
        return selectedOptionIndex;
    }

    private static void printVertical(Iterator<String> menuIter) {
        int i = 1;
        while (menuIter.hasNext()) {
            System.out.printf("%d. %s\n", i++, Text.textCapitalize(menuIter.next()));
        }
    }

    private static void printHorizontal(Iterator<String> menuIter) {
        int i = 1;
        while (menuIter.hasNext()) {
            System.out.printf("\t%d. %s\t", i++, Text.textCapitalize(menuIter.next()));
            if (!menuIter.hasNext()) {
                System.out.println();
            }
        }
    }

    private static int getInput(int menuItemCount) {
        System.out.print("\n>_ ");
        String input = read.next();
        int selectedOptionIndex;
        try {
            selectedOptionIndex = Integer.parseInt(input) - 1;
            if (selectedOptionIndex >= menuItemCount || selectedOptionIndex < 0) {
                System.out.println("\nError! Invalid Selection");
                return -1;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nError! Invalid Input");
            return -1;
        } finally {
            read.nextLine();
        }

        return selectedOptionIndex;
    }
}
