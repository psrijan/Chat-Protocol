/**
 * -----ClientMenuUtil-----
 * Client menu that is used to display the
 * UI Main Menu for Chat User .
 * 03/13/2020
 *
 */

package com.srijan.pandey.client.util;


import java.util.Scanner;

public class ClientMenuUtil {
    public static String mainMenu() {
        System.out.println("-------- Main Menu -------- ");
        System.out.println("1. Exit ");
        System.out.println("2. List Available Users/Groups");
        System.out.println("3. Chat ");
        System.out.println("4. Version ");
        return getInputStr();
    }

    public  static String getInputStr() {
        try {
            Scanner sc = new Scanner(System.in);
            return sc.nextLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
