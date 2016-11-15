package it.diamonds.engine;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Config
{
    private Properties propertiesList;

    private Properties keysList;


    public Config(String filePath, String keysFilePath)
    {
        try
        {
            propertiesList = new Properties();
            propertiesList.load(new FileInputStream(filePath));
        }
        catch(IOException e)
        {
            throw new RuntimeException(
                "The file containing the configuration does not exists");
        }

        try
        {
            keysList = new Properties();
            keysList.load(new FileInputStream(keysFilePath));
        }
        catch(IOException e)
        {
            throw new RuntimeException(
                "The file containing the keys configuration does not exists");
        }
    }


    public static Config create()
    {
        return new Config("data/GameConfig", "data/KeysConfig");
    }


    public int getInteger(String key)
    {
        try
        {
            return Integer.parseInt(propertiesList.getProperty(key));
        }
        catch(Exception e)
        {
            throw new RuntimeException("the property `" + key
                + "` does not exist or is not an integer");
        }
    }


    public void setIntegerProperty(String key, int value)
    {
        try
        {
            propertiesList.setProperty(key, "" + value);
        }
        catch(Exception e)
        {
            throw new RuntimeException("the property " + key
                + " couldn't be set to " + value);
        }
    }


    public String getKey(String key)
    {
        try
        {
            return keysList.getProperty(key);
        }
        catch(Exception e)
        {
            throw new RuntimeException("the property '" + key
                + "' does not exist");
        }
    }

}
