package com.absio.shell;

import com.absio.provider.ServerProvider;

import java.security.NoSuchAlgorithmException;

public class AbsioServerProvider {
    public static ServerProvider INSTANCE;

    static {
        try {
            INSTANCE = new ServerProvider();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
