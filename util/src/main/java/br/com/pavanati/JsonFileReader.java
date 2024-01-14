package br.com.pavanati;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class JsonFileReader {

    public String read(String path) throws IOException {
        InputStream inputStream = new ClassPathResource(path).getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

}
