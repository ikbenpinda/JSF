package w2_third_application;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class W2_third_application {
    
    public static Properties appProps;
    
    public static void main(String[] args){
        System.out.println("Starting third application.");
        
        appProps = new Properties();
        
        int odd = 0;
        boolean even = (args.length%2 == 0);
        if(!even){
            odd = -1;
            System.out.println("Detected an odd number of arguments. "+args[args.length - 1]+" will not be used.");
        }
        
        if(args.length > 0){
            for(int i = 0; i < args.length + odd; i+=2){
                appProps.put(args[i], args[i+1]);
            }
        }else{
            System.out.println("This program does nothing without arguments!");
        }
        
        saveProperties();
        
        System.out.println("Done.");
    }
    
    public static void saveProperties(){
        try(FileOutputStream out = new FileOutputStream("/home/jsf3/app.properties")){
            appProps.store(out, "---Properties file---");
            out.close();
        }catch(IOException e){System.out.println("Failed to save properties");}
    }
    
    
}
