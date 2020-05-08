package com.example.android.digilib;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from Google Books API.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if(url==null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            int response = urlConnection.getResponseCode();
            if( response == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            }
            else
                Log.e(LOG_TAG, "Error Response code: " + response);
        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }




    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Book> extractBooks(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Book> books = new ArrayList<Book>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject rootObject = new JSONObject(jsonResponse);
            JSONArray items = rootObject.optJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject book = items.getJSONObject(i);
                JSONObject bookinfo = book.optJSONObject("volumeInfo");

                String title = bookinfo.optString("title");
                JSONArray bookauthors = bookinfo.optJSONArray("authors");
                String[] authors;
                if(bookauthors != null) {
                     authors = new String[bookauthors.length()];
                    for (int j = 0; j < bookauthors.length(); j++) {
                        authors[j] = bookauthors.optString(j);
                    }
                }
                else {
                    authors = new String[1];
                    authors[0] = "N/A";
                }
                String date = bookinfo.optString("publishedDate");
                JSONObject imageLink = bookinfo.optJSONObject("imageLinks");
                String imageUrl="";
                if(imageLink!=null)
                {
                    imageUrl = imageLink.optString("smallThumbnail");
                    Log.i(LOG_TAG,"IMAGE URL:" + imageUrl);
                }

                String url = bookinfo.optString("infoLink");

                books.add(new Book(title, authors, date,imageUrl, url));
            }


            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }


    /**
     * Query the USGS dataset and return a list of {@link Book} objects.
     */
    public static List<Book> fetchBookData(String requestUrl) {


        Log.i(LOG_TAG,"Here in fetchEarthquakeData method");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Book> books = extractBooks(jsonResponse);

        // Return the list of {@link Earthquake}s
        return books;
    }

}

