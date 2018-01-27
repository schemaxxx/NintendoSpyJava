package org.schema.nintendospy.gui;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.schema.nintendospy.controller.Button;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Skin {
	public String type;
	public String name;
	public String backgroundFilename;
	public BufferedImage background;
	
	public final HashMap<Button, SkinButton> buttons = new HashMap<Button, SkinButton>();
	private File folder;
	
	public static class SkinButton{
		Button button;
		String name;
		String fileName;
		BufferedImage image;
		int x;
		int y;
		int width;
		int height;
		
//		<button name="a" image="circle.png" x="189" y="61" width="24" height="24" />
//	    <button name="b" image="circle.png" x="158" y="61" width="24" height="24" />
//
//	    <button name="start" image="circle.png" x="116" y="64" width="28" height="17" />
//	    <button name="select" image="circle.png" x="86" y="64" width="28" height="17" />
//
//	    <button name="up" image="circle.png" x="42" y="41" width="16" height="16" />
//	    <button name="down" image="circle.png" x="42" y="69" width="16" height="16" />
//	    <button name="left" image="circle.png" x="28" y="55" width="16" height="16" />
//	    <button name="right" image="circle.png" x="56" y="55" width="16" height="16" />
		public void parse(Node item, File folder) throws IOException {
			name = item.getAttributes().getNamedItem("name").getNodeValue();			
			fileName = item.getAttributes().getNamedItem("image").getNodeValue();
			x = Integer.parseInt(item.getAttributes().getNamedItem("x").getNodeValue());
			y = Integer.parseInt(item.getAttributes().getNamedItem("y").getNodeValue());
			width = Integer.parseInt(item.getAttributes().getNamedItem("width").getNodeValue());
			height = Integer.parseInt(item.getAttributes().getNamedItem("height").getNodeValue());
			
			image = ImageIO.read(new File(folder, fileName));
			
			for(Button b : Button.values()){
				if(b.name().toLowerCase(Locale.ENGLISH).equals(name.toLowerCase(Locale.ENGLISH))){
					button = b;
					break;
				}
			}
			if(button == null){
				throw new IOException("no button: "+name);
			}
		}
	}
	
	
	
	public Skin(File xml) throws ParserConfigurationException, SAXException, IOException{
		parse(xml);
	}



	public void parse(File xml) throws ParserConfigurationException, SAXException, IOException {
		this.folder = xml.getParentFile();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		BufferedInputStream bb = new BufferedInputStream(new FileInputStream(xml));
		Document doc = dBuilder.parse(bb);
		bb.close();
		
		this.name = doc.getDocumentElement().getAttribute("name");
		this.type = doc.getDocumentElement().getAttribute("type");
		
		
		NodeList childNodes = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {

			Node item = childNodes.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				if(item.getNodeName().toLowerCase(Locale.ENGLISH).equals("background")){
					backgroundFilename = item.getAttributes().getNamedItem("image").getNodeValue();
					background = ImageIO.read(new File(folder, backgroundFilename));
				}
				if(item.getNodeName().toLowerCase(Locale.ENGLISH).equals("button")){
					SkinButton b = new SkinButton();
					b.parse(item, folder);
					buttons.put(b.button, b);
				}
			}
		}
		
		
	}
}
