package com.mkyong.helloworld;

import java.util.ArrayList;
import java.util.List;

public class LambdaUsage {

    private int i = 0;

    public void nothing() {
        List<String> l = new ArrayList<>();
        l.forEach(s -> {
            boolean b = s.equals("none");
            if (b) {
              i++;
            }
        });
    }

}
