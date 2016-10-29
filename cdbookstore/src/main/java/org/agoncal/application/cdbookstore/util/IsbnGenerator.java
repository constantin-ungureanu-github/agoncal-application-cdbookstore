package org.agoncal.application.cdbookstore.util;

import java.util.Random;

@ThirteenDigits
public class IsbnGenerator implements NumberGenerator {
    private static final long serialVersionUID = 1L;

    @Override
    public String generateNumber() {
        return "13-" + new Random().nextInt();
    }
}
