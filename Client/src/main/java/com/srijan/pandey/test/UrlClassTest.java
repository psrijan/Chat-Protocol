package com.srijan.pandey.test;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlClassTest {

    public static void main(String[] args ) throws MalformedURLException {

        URL url = new URL("http://www.google.com");
        System.out.println(url.getProtocol());
        System.out.println(url.getPort());
        System.out.println(url.getHost());
        System.out.println(url.getFile());
        System.out.println(url.toExternalForm());

    }
}
