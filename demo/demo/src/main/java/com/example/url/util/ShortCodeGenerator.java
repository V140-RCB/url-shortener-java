package com.example.url.cloud.util;

@Component
public class ShortCodeGenerator {
    public String generate(String url) {
        return Integer.toHexString(url.hashCode()).substring(0, 6);
    }
}
