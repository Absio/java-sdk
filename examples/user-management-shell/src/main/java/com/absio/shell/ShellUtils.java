package com.absio.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {
    public static String promptUser(String prompt) {
        System.out.println(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String systemIn = null;
        try {
            systemIn = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return systemIn;
    }
}
