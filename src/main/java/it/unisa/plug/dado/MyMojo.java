/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.plug.dado;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 *
 * @author Rembor
 */
@Mojo(name = "hello")
public class MyMojo extends AbstractMojo {

    @Parameter(property = "msg")

    /**
     * My File.
     *
     * @parameter msg tiene la path del pom del progetto che si prende
     */
    private String msg;

    @Override
    public void execute()
            throws MojoExecutionException {

        try {
            getLog().info("Hello " + msg);
            openFile(msg);
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(MyMojo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private void openFile(String msg) throws IOException, ParserConfigurationException, SAXException {
        try {
            /*
            serve per pulire il file se già esiste
            */
            File file1 = new File("matrice.csv");
            if (file1.exists()) {
                PrintWriter writer = new PrintWriter(file1);
                writer.print("");
                writer.close();
            }
            File file = new File("C:\\Users\\Rembor\\Documents\\NetBeansProjects\\dado\\testSuite.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder;

            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("TestCase");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String classe = eElement.getElementsByTagName("Class").item(0).getTextContent();

                    String method = eElement.getElementsByTagName("method").item(0).getTextContent();
                    /*
                     Serve per evitare il bug dovuto al fatto che il plugin non riconosce se sia mvn.bat o 
                     mvn.cmd funziona solo per windows
                     */
                    String mvn = getMvnCommand();
                    Process tr = Runtime.getRuntime().exec(mvn + " clean verify -f " + msg + "\\pom.xml -Dtest=" + classe + "#" + method);
                    getLog().info("Hello  lanciaato il comando" + mvn + " clean verify -f " + msg + "\\pom.xml -Dtest=" + classe + "#" + method);
       //se non hai l'ssd metto in attesa il processo altrimenti non si ha il tempo di creare il jacoco.xml         
                    BufferedReader stdOut = new BufferedReader(new InputStreamReader(tr.getInputStream()));
                    String s;
                    while ((s = stdOut.readLine()) != null) {
      //tempo morto
                    }

                    readJacoco(msg);

                }

            }

        } catch (ParserConfigurationException | SAXException ex) {
            Logger.getLogger(MyMojo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String getMvnCommand() {
        String mvnCommand = "mvn";
        if (File.separatorChar == '\\') {
            mvnCommand = findExecutableOnPath("mvn.cmd");
            if (mvnCommand == null) {
                mvnCommand = findExecutableOnPath("mvn.bat");
            }
        }
        return mvnCommand;
    }

    public static String findExecutableOnPath(String name) {
        for (String dirname : System.getenv("PATH").split(File.pathSeparator)) {
            File file = new File(dirname, name);
            if (file.isFile() && file.canExecute()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    private void readJacoco(String msg) {
        try {
            File fXmlFile = new File(msg + "\\target\\site\\jacoco\\jacoco.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);

            dbf.setNamespaceAware(true);
            dbf.setFeature("http://xml.org/sax/features/namespaces", false);
            dbf.setFeature("http://xml.org/sax/features/validation", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(fXmlFile);

            NodeList nList3 = doc.getElementsByTagName("sourcefile");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList3.getLength(); temp++) {

                Node nNode3 = nList3.item(temp);

                if (nNode3.hasAttributes()) {

                    // get attributes names and values
                    NamedNodeMap nodeMap3 = nNode3.getAttributes();

                    for (int i = 0; i < nodeMap3.getLength(); i++) {

                        Node node = nodeMap3.item(i);

                        getLog().info("attr name : " + node.getNodeName());
                        getLog().info(" attr value : " + node.getNodeValue());
                    }

                }

                System.out.println("----------------------------");
                if (nNode3.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement3 = (Element) nNode3;
                    NodeList list3 = eElement3.getElementsByTagName("line");

                    System.out.println(list3.getLength());

                    ArrayList<Integer> stringa = new ArrayList<>();

                    for (int count = 0; count < list3.getLength(); count++) {
                        System.out.println(list3.item(count).getAttributes().getNamedItem("ci").getNodeValue());

                        Node tempNode = list3.item(count);

                        // make sure it's element node.
                        if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                            if (tempNode.hasAttributes()) {

                                // get attributes names and values
                                NamedNodeMap nodeMap4 = tempNode.getAttributes();

                                for (int i = 0; i < nodeMap4.getLength(); i++) {
                                    Node node = nodeMap4.item(i);
                                    String tes = "ci";
                                    //System.out.println("attr name : " + node.getNodeName() + "  attr value : " + node.getNodeValue());

                                    if (tes.equals(node.getNodeName())) {
                                        Node node1 = node;
                                        // System.out.println("attr name : " + node1.getNodeName() + "  attr value : " + node1.getNodeValue());

                                        if (Integer.parseInt(node1.getNodeValue()) != 0) {

                                            stringa.add(1);
                                        } else {
                                            stringa.add(0);

                                        }

                                    }

                                }

                            }

                        }

                    }

                   // WriteCvs.writeDataAtOnce(stringa);

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException | NumberFormatException e) {
        }

    }

}
