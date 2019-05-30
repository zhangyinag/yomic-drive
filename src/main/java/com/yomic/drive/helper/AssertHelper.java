package com.yomic.drive.helper;

public class AssertHelper {
    public static void assertNotNull(Object... obj) throws RuntimeException{
        for (Object o : obj) {
            if(o == null) throw new RuntimeException("assert null fail");
        }
    }
}
