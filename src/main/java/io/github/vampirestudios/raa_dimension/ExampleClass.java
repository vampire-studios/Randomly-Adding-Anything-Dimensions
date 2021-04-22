package io.github.vampirestudios.raa_dimension;

public class ExampleClass {

    private static String exampleString;

    public ExampleClass() {
        exampleString = "Test";
    }

    static {
        if(exampleString.equals("Test")) {
            System.out.println(exampleString);
        }
    }

}
