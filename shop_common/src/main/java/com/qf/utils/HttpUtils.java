package com.qf.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class HttpUtils {


    public static void sendRequet(String urlStr){
        try {
            URL url =new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            int responseCode = urlConnection.getResponseCode();
            if (responseCode==200){
                Scanner scanner = new Scanner(urlConnection.getInputStream());
                while (scanner.hasNextLine()){
                    System.out.println(scanner.nextLine());
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
