package pl.kurs.task2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
public class CurrencyService {
    private final String apiURL = "https://api.apilayer.com/exchangerates_data/";
    private final String apiKey = "BnW7ENBiBLBknHJSmAIOb4xax9pdohFL";
    private final Map<String, CachedRate> cache = new ConcurrentHashMap<>();
    private final Map<String, Lock> locks = new ConcurrentHashMap<>();
    private final long cacheTtlMillis = 10000;

    private HttpURLConnection httpURLConnection;

    public CurrencyService(HttpURLConnection httpURLConnection) {
        this.httpURLConnection = httpURLConnection;
    }

    private static class CachedRate {
        double rate;
        long timestamp;

        CachedRate(double rate, long timestamp) {
            this.rate = rate;
            this.timestamp = timestamp;
        }
    }

    public double exchange(String currencyFrom, String currencyTo, double amount) throws Exception {
        String cacheKey = currencyFrom + "_" + currencyTo;
        long currentTime = System.currentTimeMillis();

        CachedRate cachedRate = cache.get(cacheKey);
        if (cachedRate != null && (currentTime - cachedRate.timestamp) < cacheTtlMillis) {
            return amount * cachedRate.rate;
        }

        Lock lock = locks.computeIfAbsent(cacheKey, k -> new ReentrantLock());
        lock.lock();
        try {
            cachedRate = cache.get(cacheKey);
            if (cachedRate != null && (currentTime - cachedRate.timestamp) < cacheTtlMillis) {
                return amount * cachedRate.rate;
            }
            double newRate = fetchExchangeRateFromApi(currencyFrom, currencyTo);
            updateCache(cacheKey, newRate);
            return amount * newRate;
        } finally {
            lock.unlock();
            locks.remove(cacheKey);
        }
    }

    protected double fetchExchangeRateFromApi(String currencyFrom, String currencyTo) throws Exception {
        String endpoint = apiURL + "convert?to=" + currencyTo + "&from=" + currencyFrom + "&amount=1";
        URL url = new URL(endpoint);

        if (httpURLConnection == null) {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        }
        httpURLConnection.setRequestProperty("apikey", apiKey);

        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed to fetch exchange rate, HTTP response code: " + responseCode);
        }

        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = httpURLConnection.getInputStream()) {
            JsonNode response = mapper.readTree(inputStream);

            if (response.has("result")) {
                return response.get("result").asDouble();
            } else {
                throw new RuntimeException("Failed to fetch exchange rate from API.");
            }
        }
    }

    private void updateCache(String cacheKey, double newRate) {
        cache.put(cacheKey, new CachedRate(newRate, System.currentTimeMillis()));
    }

    protected void updateCacheForTesting(String cacheKey, double rate, long timestamp) {
        cache.put(cacheKey, new CachedRate(rate, timestamp));
    }

}
