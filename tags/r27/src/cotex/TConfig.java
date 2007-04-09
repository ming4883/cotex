/*
 * TConfig.java
 *
 * Created on April 3, 2007, 10:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import org.jdom.input.*;
import org.jdom.filter.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Ming
 */
public class TConfig {
    
    HashMap<String, String> mMap = null;
    String mFilename;
    
    /** Creates a new instance of TConfig */
    
    public TConfig(String filename) {
        
        mFilename = filename;
        mMap = new HashMap<String, String>();
        
        java.io.FileReader reader = null;
        
        try {
            reader = new java.io.FileReader(filename);
            
            SAXBuilder builder = new SAXBuilder();
                
            org.jdom.Document xmlDoc = builder.build(reader);

            Iterator iterSection = xmlDoc.getRootElement().getChildren("section").iterator();

            while( iterSection.hasNext() ) {
                org.jdom.Element section = (org.jdom.Element)iterSection.next();

                String sectionName = section.getAttributeValue("name");
                //console.logMessage( "section = " + sectionName );

                Iterator iterSetting = section.getChildren("setting").iterator();

                while( iterSetting.hasNext() ) {

                    org.jdom.Element setting = (org.jdom.Element)iterSetting.next();

                    String key = setting.getAttributeValue("key");
                    String val = setting.getAttributeValue("value");

                    //console.logMessage( key + ": " + val );
                    mMap.put(sectionName + "." + key, val);
                }
            }
            
        }
        catch (Exception e) {
            
        }
        
    }
    
    public String getSetting(String section, String key) {
       
        String val = mMap.get(section + "." + key);
        
        if(val == null)
            return "";
        
        return val;
    }
    
    public void setSetting(String section, String key, String val) {
    
        mMap.put(section + "." + key, val);
    
        saveToFile();
    }
    
    private void saveToFile() {
        
        //mFilename
    }
    
}
