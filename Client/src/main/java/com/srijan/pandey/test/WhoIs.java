package com.srijan.pandey.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class WhoIs {
    public static void main(String[] args) throws Exception {
        int c;
        Socket s = new Socket("whois.internic.net", 43);
        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();

        String str = (args.length == 0 ? "testing you" : args[0] + "\n");
        byte[] byteArr = str.getBytes();

        os.write(byteArr);

        while ((c = is.read()) != -1) {
            System.out.println((char) c);
        }
        s.close();
    }
}
