package w2_second_application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class W2_second_application {

    public static Properties appProps;
    
    public static void main(String[] args){
        System.out.println("Starting second application.");
        
        loadProperties();
        showProperties();
    }
    
    public static void loadProperties(){
        appProps = new Properties();
        try(FileInputStream in = new FileInputStream("/home/jsf3/app.properties")){
            appProps.load(in);
            in.close();
        }catch(IOException e){System.out.println("Failed to load properties");}
        
    }
    
    public static void showProperties(){
        for(Object p : appProps.keySet()){
            String prop = p.toString();
            System.out.println(prop+"="+appProps.getProperty(prop));
        }
    }
    
}
