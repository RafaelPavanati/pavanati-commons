package br.com.pavanati.test.fixture;

import org.jeasy.random.EasyRandom;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

public class Fixture {

    private static EasyRandom easyRandom = new EasyRandom();

    public XMLGregorianCalendar getRandomValue() {
        try {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(Math.abs(easyRandom.nextLong()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (final DatatypeConfigurationException dce) {
            return null;
        }
    }

    public static <T> T make(final Class<T> from) {
        return easyRandom.nextObject(from);
    }

    public static <T> List<T> make(final Class<T> from, final int size) {
        return easyRandom.objects(from, size).collect(Collectors.toList());
    }

}