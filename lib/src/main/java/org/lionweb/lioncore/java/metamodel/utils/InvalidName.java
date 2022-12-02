package org.lionweb.lioncore.java.metamodel.utils;

public class InvalidName extends RuntimeException {
    public InvalidName(String nameType, String value) {
        super("The given name is not a valid " + nameType + ". Value: '" + value + "'");
    }
}