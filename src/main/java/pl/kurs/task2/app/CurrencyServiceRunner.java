package pl.kurs.task2.app;

import pl.kurs.task2.service.CurrencyService;

public class CurrencyServiceRunner {
    public static void main(String[] args) throws Exception {

        CurrencyService service = new CurrencyService();

        double exchange = service.exchange("PLN", "USD", 5000);

        System.out.println(exchange);
    }
}
