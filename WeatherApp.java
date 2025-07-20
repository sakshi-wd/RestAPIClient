import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.Scanner;

/**
 * A simple Java console application that fetches weather data from
 * OpenWeatherMap API and displays temperature, humidity, weather description, and wind speed.
 */
public class WeatherApp {

    // TODO: Replace with your actual OpenWeatherMap API key
    private static final String API_KEY = "YOUR_API_KEY_HERE";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter a city name
        System.out.print("Enter city name: ");
        String city = scanner.nextLine();

        try {
            // Construct the API request URL
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" 
                               + city + "&appid=" + API_KEY + "&units=metric";

            // Create a URL object and open an HTTP connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set the request method to GET (to retrieve data)
            conn.setRequestMethod("GET");
            conn.connect();

            // Check for a valid response code (HTTP 200 means success)
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Error: Failed to get data. HTTP code: " + responseCode);
                return;
            }

            // Read the response from the API
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            JSONObject json = new JSONObject(response.toString());

            // Extract and display data
            String cityName = json.getString("name"); // city name
            JSONObject main = json.getJSONObject("main"); // contains temperature and humidity
            JSONObject weather = json.getJSONArray("weather").getJSONObject(0); // weather description
            JSONObject wind = json.getJSONObject("wind"); // wind speed

            // Print weather information
            System.out.println("\n===== Weather Report for " + cityName + " =====");
            System.out.println("Temperature: " + main.getDouble("temp") + " °C");
            System.out.println("Humidity: " + main.getInt("humidity") + "%");
            System.out.println("Weather: " + weather.getString("description"));
            System.out.println("Wind Speed: " + wind.getDouble("speed") + " m/s");

        } catch (Exception e) {
            // Handle any exceptions such as network or parsing errors
            System.out.println("Exception: " + e.getMessage());
        }

        // Close the scanner object to avoid resource leak
        scanner.close();
    }
}
