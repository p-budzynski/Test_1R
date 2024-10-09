package pl.kurs.task2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest {
    @Mock
    private HttpURLConnection httpURLConnectionMock;

    @InjectMocks
    private CurrencyService currencyServiceMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCachedExchangeRateWhenCacheIsValid() throws Exception {
        //given
        currencyServiceMock = spy(currencyServiceMock);
        doReturn(4.0).when(currencyServiceMock).fetchExchangeRateFromApi("USD", "PLN");

        //when
        double result1 = currencyServiceMock.exchange("USD", "PLN", 100);

        //then
        assertEquals(400.0, result1);

        //when
        double result2 = currencyServiceMock.exchange("USD", "PLN", 100);

        //then
        assertEquals(400.0, result2);
        verify(currencyServiceMock, times(1)).fetchExchangeRateFromApi("USD", "PLN");
    }

    @Test
    void shouldFetchNewExchangeRateWhenCacheExpires() throws Exception {
        //given
        currencyServiceMock = spy(currencyServiceMock);
        doReturn(4.0).when(currencyServiceMock).fetchExchangeRateFromApi("USD", "PLN");

        //when
        double result1 = currencyServiceMock.exchange("USD", "PLN", 100);

        //then
        assertEquals(400.0, result1);

        //given
        currencyServiceMock.updateCacheForTesting("USD_PLN", 4.0, System.currentTimeMillis() - 11000);
        doReturn(4.5).when(currencyServiceMock).fetchExchangeRateFromApi("USD", "PLN");

        //when
        double result2 = currencyServiceMock.exchange("USD", "PLN", 100);

        //then
        assertEquals(450.0, result2);
        verify(currencyServiceMock, times(2)).fetchExchangeRateFromApi("USD", "PLN");
    }

    @Test
    void shouldReturnCorrectExchangeRateFromApi() throws Exception {
        //given
        currencyServiceMock = spy(currencyServiceMock);
        String jsonResponse = "{ \"result\": 4.0 }";
        InputStream inputStreamMock = new ByteArrayInputStream(jsonResponse.getBytes());

        when(httpURLConnectionMock.getInputStream()).thenReturn(inputStreamMock);
        when(httpURLConnectionMock.getResponseCode()).thenReturn(200);

        //when
        double result = currencyServiceMock.fetchExchangeRateFromApi("USD", "PLN");

        //then
        assertEquals(4.0, result);
    }

    @Test
    void shouldFetchExchangeRateOnlyOnceInConcurrentAccess() throws Exception {
        //given
        currencyServiceMock = spy(currencyServiceMock);
        doReturn(4.0).when(currencyServiceMock).fetchExchangeRateFromApi("USD", "PLN");

        //when
        Runnable task = () -> {
            try {
                currencyServiceMock.exchange("USD", "PLN", 100);
            } catch (Exception e) {
                fail(e);
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        //then
        verify(currencyServiceMock, times(1)).fetchExchangeRateFromApi("USD", "PLN");
    }

    @Test
    void shouldThrowExceptionWhenApiFails() throws Exception {
        //given
        when(httpURLConnectionMock.getResponseCode()).thenReturn(500);

        //when then
        assertThatThrownBy(() -> currencyServiceMock.fetchExchangeRateFromApi("USD", "PLN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to fetch exchange rate, HTTP response code: 500");
    }

    @Test
    void shouldThrowExceptionWhenApiResponseIsInvalid() throws Exception {
        //given
        String invalidJsonResponse = "{ \"invalid_key\": 4.0 }";
        InputStream inputStreamMock = new ByteArrayInputStream(invalidJsonResponse.getBytes());

        when(httpURLConnectionMock.getInputStream()).thenReturn(inputStreamMock);
        when(httpURLConnectionMock.getResponseCode()).thenReturn(200);

        //when then
        assertThatThrownBy(() -> currencyServiceMock.fetchExchangeRateFromApi("USD", "PLN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to fetch exchange rate from API.");
    }

    @Test
    void shouldUseDefaultConstructor() {
        //given
        CurrencyService currencyService = new CurrencyService();

        //then
        assertNotNull(currencyService);
    }

}
