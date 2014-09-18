package w2_eerste_applicatie;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class W2_eerste_applicatie {

    public static Properties appProps;
    public static String testEnviron;
    
    public static void main(String[] args){
        System.out.println("Starting first application.");
        
        getEnvironmentVariable();
        setProperties();
        saveProperties();
        
    }
    
    public static void setProperties(){
        appProps = new Properties();
        appProps.put("TestEnviron", testEnviron);
    }
    
    public static void getEnvironmentVariable(){
        Map<String, String> env = System.getenv();
        
        for(String envName : env.keySet()){
            if(envName.equals("TestEnviron")){
                testEnviron = env.get(envName);
                System.out.println("First application found: "+envName+"="+testEnviron);
            }
        }
    }
    
    public static void saveProperties(){
        try(FileOutputStream out = new FileOutputStream("/home/jsf3/app.properties")){
            appProps.store(out, "---Properties file---");
            out.close();
        }catch(IOException e){System.out.println("Failed to save properties");}
    }
    
}
