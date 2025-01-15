package Lenovo.pl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Currency to convert: ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.println("Target currency: ");
        String toCurrency = scanner.nextLine().toUpperCase();

        System.out.println("quota: ");
        double quota = scanner.nextDouble();

        try {
            Properties properties = new Properties();
            FileInputStream configFile = new FileInputStream("C:\\Users\\LENOVO\\Desktop\\CurrencyConverter\\CurrencyConcerter\\src\\main\\resources\\config.properties");
            properties.load(configFile);
            configFile.close();

            String apiUrl = properties.getProperty("api.url");

            String requestUrl = String.format("%s/%s", apiUrl, fromCurrency);
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Scanner apiScanner = new Scanner(connection.getInputStream());
            StringBuilder sb = new StringBuilder();
            while (apiScanner.hasNext()){
                sb.append(apiScanner.nextLine());
            }
            apiScanner.close();

            ObjectMapper mapper = new ObjectMapper();
            ExchangeRates rates = mapper.readValue(sb.toString(), ExchangeRates.class);

            Double exchangeRate = rates.getRates().get(toCurrency);
            if (exchangeRate == null){
                System.out.println("Wrong currency");
            }
            else {
                double convertQuota = quota * exchangeRate;
                System.out.println("Result: " + convertQuota + " " + toCurrency);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}