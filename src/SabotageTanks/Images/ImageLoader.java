/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Images;

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

public class ImageLoader extends DefaultHandler
{
    private static final String TAG_TANK = "tank",
                                TAG_IMAGE = "image";
    
    private static final String XML_PATH_BEGIN = "../Images/";
    private static final String IMAGE_PATH_BEGIN = "Images/";    
    private static String XML_TANKS = "TankImages.xml";
    private List<TankImageResources> tanksResources;
    private List<TankAnimator> tankAnimators;
    
    private String currentElement;
    private TankImageResources currentTankResources;
    private TankImage currentTankImage;
    
    public ImageLoader()
    {
        TankInfoParser parser = new TankInfoParser();
        parser.loadTanksInfo();
        
        tankAnimators = parser.getTankAnimators();
    }
    
    public TankAnimator getTankAnimator(String tankName)
    {
        for (TankImageResources resource : tanksResources)
        {
            if (resource.name.matches(tankName))
            {
                return new TankAnimator(resource);
            }
        }
        return null;
    }
    
    public class TankImage
    {
        public final String name;
        public BufferedImage image;
        
        public TankImage(String imageName)
        {
            this.name = imageName;
        }
    }
    public class TankImageResources
    {
        public final String name;
        public List<TankImage> images;
        
        public TankImageResources(String tankName)
        {
            name = tankName;
            this.images = new ArrayList<>();
        }
    }
    
    private class TankInfoParser extends DefaultHandler
    {
        List<TankAnimator> tankAnimators;
        
        TankAnimator current
        
        void loadTanksInfo()
        {
            try {
                tanksResources = new ArrayList<>();
                tankAnimators = new ArrayList<>();
                
                SAXParserFactory spf = SAXParserFactory.newInstance();
                spf.setNamespaceAware(true);
                SAXParser saxParser = spf.newSAXParser();

                URL tanksXMLurl = getClass().getResource(XML_PATH_BEGIN + XML_TANKS);
                saxParser.parse(tanksXMLurl.getFile(), this);

            } catch (SAXException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        List<TankAnimator> getTankAnimators()
        {
            return tankAnimators;
        }
        
        @Override
        public void startDocument() throws SAXException
        {
            super.startDocument(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            currentElement = localName;

            switch (localName)
            {
                case TAG_TANK:
                    String tankName = attributes.getValue("name");
                    currentTankResources = new TankImageResources(tankName);
                    break;
                case TAG_IMAGE:
                    String imageName = attributes.getValue("name");
                    currentTankImage = new TankImage(imageName);
                    break;
            }
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            currentElement = "";

            switch (localName)
            {
                case TAG_TANK:
                    tanksResources.add(currentTankResources);
                    currentTankResources = null;
                    break;
                case TAG_IMAGE:
                    currentTankResources.images.add(currentTankImage);
                    currentTankImage = null;
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
                        URL url = getClass().getResource(XML_PATH_BEGIN + fileName);
                        currentTankImage.image = ImageIO.read(url);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            }
        }
    }
}
