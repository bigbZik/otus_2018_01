package ru.otus.hw01;

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import java.io.File;

public class Main {

    public static void main(String[] args) {

        String filename = "hw01/text.txt";

        File file = new File(filename);

        CharSource source = Files.asCharSource(file, Charsets.UTF_8);
        try {
            String result = source.read();

            System.out.println("Содержание файла "+filename);
            System.out.println(result);

        } catch (java.io.IOException e) {
            System.out.println("Ошибка чтения файла "+filename);
        }

    }
}