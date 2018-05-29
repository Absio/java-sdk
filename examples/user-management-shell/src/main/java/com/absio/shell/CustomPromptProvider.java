package com.absio.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomPromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        try {
            if (AbsioProvider.INSTANCE.isAuthenticated()) {
                return new AttributedString(AbsioProvider.INSTANCE.getUserId() + ":>",
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
            }
            else if (AbsioProvider.INSTANCE.isInitialized()) {
                return new AttributedString("absio-" + getProviderTypeInitials() + ":>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
            }
            else {
                return new AttributedString("shell:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
            }
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
        //initialize https://apidev.absio.com 62883408-7ff0-49c4-a48e-f3146891b486 | login --user-id fa49c241-c208-40f4-8d85-58a2d27e42b4 --passphrase passphrase#2
    }

    private String getProviderTypeInitials() {
        switch (AbsioProvider.INSTANCE.getProviderType()) {
            case SERVER:
                return "SP";
            case SERVER_CACHE_OFS:
                return "SCOP";
            case OFS:
                return "OP";
        }
        return null;
    }
}
