package com.yomic.drive.helper;

import java.util.function.Supplier;

public class ExceptionHelper {
    public static Supplier<RuntimeException> optionalThrow(String msg) {
        return () -> new RuntimeException(msg);
    }
}
