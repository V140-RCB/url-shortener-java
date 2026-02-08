package com.example.url.cloud.store;

@Component
public class UrlStore {
    public Map<String, String> shortToLong = new ConcurrentHashMap<>();
    public Map<String, String> longToShort = new ConcurrentHashMap<>();
    public Map<String, Integer> domainCount = new ConcurrentHashMap<>();
}
