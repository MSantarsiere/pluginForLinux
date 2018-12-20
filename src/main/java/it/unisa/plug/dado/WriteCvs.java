/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.plug.dado;

/**
 *
 * @author Rembor
 */
import it.unisa.prioritization.runner.AS;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Crea la matrice di copertura e riordina la testsuite
 *
 *
 * @author Rembor
 */
public class WriteCvs {

    // stampa il csv nella base directory del progetto
    /**
     * @param file contiene il file della matrice
     * @param file1 contiene il file del costo della matrice
     */
    static private File file = new File("matrice.csv");
    static private File file1 = new File("matricecosti.csv");
    static ArrayList<ArrayList<Integer>> matrice = new ArrayList<ArrayList<Integer>>();

    /**
     * Crea la matrice di copertura
     *
     * @param linea contiene la lista dei valori di copertura, 0 per non coperto
     * 1 per coperto
     * @param fw contien la matrice
     * @param bw contiene il buffer
     * @param out contiene l'output
     *
     */
    public static void writeDataAtOnce(ArrayList<Integer> linea) {
        matrice.add(linea);
        try {

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            for (int i = 0; i < linea.size(); i++) {
                System.out.print(linea.get(i) + "  ");
                int indice = linea.get(i);

                out.print(indice + " ");
            }
            out.println();
            out.flush();

        } catch (IOException ex) {
            Logger.getLogger(WriteCvs.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Crea la matrice di costi
     *
     * @param writer contiene il file
     * @param br contiene il buffer
     * @param fr contiene lo strumento per leggere il file
     * @param fw contien la matrice
     * @param bw contiene il buffer
     * @param out contiene l'output
     * @param scanner contiene il valore del stringa
     *
     */
 public   static void createCostMatrix() {
        if (file1.exists()) {
            PrintWriter writer;
            try {
                writer = new PrintWriter(file1);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(WriteCvs.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        BufferedReader br = null;
        FileReader fr = null;

        try {

            FileWriter fw = new FileWriter(file1, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            BufferedReader r = new BufferedReader(new FileReader(file));
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int count = StringUtils.countMatches(line, "1");
                out.println(count);
                out.flush();

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null) {
                    br.close();
                }

                if (fr != null) {
                    fr.close();
                }

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

    /**
     * Riorganizza la test suite in base al contenuto del file Var.txt
     *
     * @param permutazioni contine il file Var
     * @param array legge i valori presenti in Var
     * @param r contiene il file Var
     * @param scanner contiene il valore di Var
     * @param line contiene la linea presente in Var
     * @param documentFactory contiene l'istanza xml
     * @param documentBuilder contiene il costruttore del file xml
     * @param document1 contiene la nuova TestSuite
     * @param xmlFile contiene il file della TestSuite
     * @param root contiene la root del file xml
     * @param nodeList contiene il tag Test Case
     * @param attr contiene l'attributo id
     * @param firstName contiene l'attributo classe
     * @param lastname contine l'attributo method
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    static void createNewXMl() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        File permutazioni = new File("VAR.txt");

        String[] array = null;
        BufferedReader r = new BufferedReader(new FileReader(permutazioni));
        Scanner scanner = new Scanner(permutazioni);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            array = line.split(" ", -1);
        }

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document1 = documentBuilder.newDocument();

        // root element
        Element root = document1.createElement("testSuite");
        document1.appendChild(root);

        for (int i = 0; i < array.length; i++) {

        }

        File xmlFile = new File("testSuite.xml");
        DocumentBuilderFactory docbuildFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docbuildFactory.newDocumentBuilder();
        Document document = docBuilder.parse(xmlFile);

        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("TestCase");

        for (int indice = 0; indice < array.length; indice++) {
            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    if (element.getAttribute("id").equals(array[indice])) {
                        Element employee = document1.createElement("TestCase");

                        root.appendChild(employee);

                        // set an attribute to staff element
                        Attr attr = document1.createAttribute("id");
                        attr.setValue(element.getAttribute("id"));
                        employee.setAttributeNode(attr);
                        Element firstName = document1.createElement("Class");
                        firstName.appendChild(document1.createTextNode(element.getElementsByTagName("Class").item(0).getTextContent()));
                        employee.appendChild(firstName);

                        // lastname element
                        Element lastname = document1.createElement("method");
                        lastname.appendChild(document1.createTextNode(element.getElementsByTagName("method").item(0).getTextContent()));
                        employee.appendChild(lastname);

                    }
                }
            }
        }

        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document1);
        StreamResult streamResult = new StreamResult(new File("testSuite.xml"));

        transformer.transform(domSource, streamResult);

        System.out.println("Done creating XML File");

    }

    /**
     * permette di stampare la matrice di copertura
     */
    static public void stampaM() {
        for (int i = 0; i < matrice.size(); i++) {
            System.out.println(matrice.get(i));
        }
    }

    /**
     * Restituisce la matrice di copertura
     *
     * @return matrice contiene la matrice di copertura
     */
    static public ArrayList<ArrayList<Integer>> getM() {
        return matrice;
    }
}
