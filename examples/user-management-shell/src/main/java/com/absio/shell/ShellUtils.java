package com.absio.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {
    public static String promptForString(String message) {
        System.out.println(message);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String systemIn = null;
        try {
            systemIn = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return systemIn;
    }

    public static boolean promptForYesOrNo(String message) {
        String response = ShellUtils.promptForString(message);
        return response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes");
    }
}
