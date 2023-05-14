package org.example;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Properties;
/**
 * class Injector implements dependency injection into any object that contains fields
 * marked with the @AutoInjectable
 */

class Injector {
    /**
     * reference to the configuration object
     */
    private Properties properties;
    /**
     * The constructor. Initializes the configuration object with the file
     * whose path is passed as a parameter
     * @param pathToPropertiesFile full path to the configuration file
     */
    Injector(String pathToPropertiesFile)  {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(pathToPropertiesFile));
        } catch (Exception e) {
            System.out.println("Error! Properties file not found!");
        }
    }

    /**
     * The default constructor. Initializes the configuration object with the file inj.properties
     */
    Injector() {
        try {
            String pathToPropertiesFile = "src\\main\\java\\org\\example\\properties\\inj";
            properties = new Properties();
            properties.load(new FileInputStream(pathToPropertiesFile));
        } catch (Exception e) {
            System.out.println("Error! Properties file not found!");
        }
    }
    /**
     * method accepts an arbitrary object, examines it for the presence of fields with an AutoInjectable annotation.
     * if there is such a field, look at its type and look for an implementation
     * in the file the reference to which is stored in the variable properties
     * @param obj an object of any class
     * @return returns an object with initialized fields with the AutoInjectable annotation
     */
    <T> T inject(T obj) throws IllegalAccessException, InstantiationException {
        Class<?> dependency;
        Class<?> cl = obj.getClass();
        Field[] fields = cl.getDeclaredFields();
        for (Field field: fields){
            Annotation a = field.getAnnotation(AutoInjectable.class);
            if (a != null){
                String[] fieldType = field.getType().toString().split(" ");
                String equalsClassName = properties.getProperty(fieldType[1], null);
                if (equalsClassName != null){
                    try {
                        dependency = Class.forName(equalsClassName);
                    } catch (ClassNotFoundException e){
                        System.out.println("Error! Not found class for " + equalsClassName);
                        continue;
                    }
                    field.setAccessible(true);
                    field.set(obj, dependency.newInstance());
                }
                else
                    System.out.println("Error! Not found properties for field type " + fieldType[1]);
            }
        }
        return obj;
    }
}
