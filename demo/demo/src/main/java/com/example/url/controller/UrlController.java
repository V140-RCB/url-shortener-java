package com.example.url.cloud.controller;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlStore store;
    private final ShortCodeGenerator generator;

    public UrlController(UrlStore store, ShortCodeGenerator generator) {
        this.store = store;
        this.generator = generator;
    }

    @PostMapping("/shorten")
    public Map<String, String> shorten(@RequestBody Map<String, String> req) {
        String url = req.get("url");

        if (store.longToShort.containsKey(url)) {
            return Map.of("shortUrl", store.longToShort.get(url));
        }

        String code = generator.generate(url);
        store.shortToLong.put(code, url);
        store.longToShort.put(url, code);

        String domain = URI.create(url).getHost();
        store.domainCount.merge(domain, 1, Integer::sum);

        return Map.of("shortUrl", code);
    }

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String url = store.shortToLong.get(code);
        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/metrics")
    public List<Map<String, Object>> metrics() {
        return store.domainCount.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(3)
                .map(e -> Map.of("domain", e.getKey(), "count", e.getValue()))
                .toList();
    }
}
