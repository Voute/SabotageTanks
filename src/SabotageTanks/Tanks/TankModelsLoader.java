/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Tanks;

import SabotageTanks.GameLog;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TankModelsLoader
{
    private static final String TAG_TANK = "tank",
                                TAG_IMAGE = "image",
                                TAG_PARAMETER = "parameter";
    
//    private static final String XML_PATH_BEGIN = "../Tanks/";  
    private static String XML_TANKS = "TankModels.xml";
    
    private List<TankModel> tankModels;
    
    public TankModelsLoader()
    {
        tankModels = new ArrayList<>();
        
        TankModelsParser parser = new TankModelsParser();
        tankModels = parser.loadTankModels();
    }
    
    public List<TankModel> getModels()
    {
        return tankModels;
    }
    
    private class TankModelsParser extends DefaultHandler
    {     
        List<TankModel> tankModels;
        
        String currentElement;
        String currentAttributeValue;
        TankModel currentTankModel;
        
        public List<TankModel> loadTankModels()
        {
            try {
                tankModels = new ArrayList<>();
                
                SAXParserFactory spf = SAXParserFactory.newInstance();
                spf.setNamespaceAware(true);
                SAXParser saxParser = spf.newSAXParser();

                URL tanksXMLurl = getClass().getResource(XML_TANKS);
                saxParser.parse(tanksXMLurl.getFile(), this);

            } catch (SAXException ex) {
                Logger.getLogger(TankModelsLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TankModelsLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(TankModelsLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return tankModels;
        }
        
        @Override
        public void startDocument() throws SAXException
        {
            super.startDocument(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument(); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            currentElement = localName;
            
            switch (localName)
            {
                case TAG_TANK:
                    String tankModel = attributes.getValue("model");
                    currentTankModel = new TankModel(tankModel);
                    break;
                case TAG_IMAGE:
                    currentAttributeValue = attributes.getValue("type");
                    break;
                case TAG_PARAMETER:
                    currentAttributeValue = attributes.getValue("name");
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            currentElement = "";
            currentAttributeValue = "";
            
            switch (localName)
            {
                case TAG_TANK:
                    tankModels.add(currentTankModel);
                    currentTankModel = null;
                    break;
            }
        }

        @Override
        public void characters(char[] arg0, int arg1, int arg2) throws SAXException
        {
            switch (currentElement)
            {
                case TAG_IMAGE:
                    String fileName = new String(arg0, arg1, arg2);
                    try {
                        
                        URL url = getClass().getResource(fileName);
                        TankImage image = new TankImage(currentAttributeValue, ImageIO.read(url));
                        currentTankModel.addImage(image);
                        
                    } catch (MalformedURLException ex) {
                        GameLog.write(ex);
                    } catch (IOException ex) {
                        GameLog.write(ex);
                    }
                    break;
                case TAG_PARAMETER:
                    TankParameter parameter = new TankParameter(currentAttributeValue, new String(arg0, arg1, arg2));
                    currentTankModel.addParameter(parameter);
                    break;
            }
        }
    }
}
