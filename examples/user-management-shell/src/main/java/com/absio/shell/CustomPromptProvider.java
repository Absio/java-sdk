package com.absio.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomPromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        if (AbsioServerProvider.INSTANCE.isAuthenticated()) {
            return new AttributedString(AbsioServerProvider.INSTANCE.getUserId() + ":>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
        }
        else if (AbsioServerProvider.INSTANCE.isInitialized()) {
            return new AttributedString("absio" + ":>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
        }
        else {
            return new AttributedString("shell:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
        }
        //initialize https://apidev.absio.com 62883408-7ff0-49c4-a48e-f3146891b486 | login --user-id fa49c241-c208-40f4-8d85-58a2d27e42b4 --passphrase passphrase#2
    }
}
