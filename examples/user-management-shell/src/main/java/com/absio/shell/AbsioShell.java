package com.absio.shell;

import org.conscrypt.OpenSSLProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class AbsioShell {

    public static void main(String[] args) {
        Security.insertProviderAt(new OpenSSLProvider(), 1);
        SpringApplication.run(AbsioShell.class, args);
    }
}
