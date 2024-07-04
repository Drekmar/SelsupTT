package org.example;

import java.util.concurrent.TimeUnit;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) throws InterruptedException {
        CrptApi crptApi = new CrptApi(10, TimeUnit.MINUTES);

        Document document = new Document();

        crptApi.createDocument(document, "your-signature-here");
    }
}


