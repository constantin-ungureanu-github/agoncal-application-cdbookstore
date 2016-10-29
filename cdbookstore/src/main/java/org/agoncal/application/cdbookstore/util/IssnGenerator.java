package org.agoncal.application.cdbookstore.util;

import java.util.Random;

@EightDigits
public class IssnGenerator implements NumberGenerator {
    private static final long serialVersionUID = 1L;

    @Override
    public String generateNumber() {
        return "8-" + (new Random().nextInt() / 1000);
    }
}
