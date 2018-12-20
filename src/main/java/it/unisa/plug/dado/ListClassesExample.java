package it.unisa.plug.dado;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Crea la test suite leggendo le classi java e i metodi presenti nel progetto
 *
 * @author Rembor
 */
public class ListClassesExample {

    static int i = 0;

    /**
     * Esplora le classi leggendo le classi e i metodi presenti nel progetto
     *
     * @param msg contiene la path assoluta del progetto
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public static void listClasses(String msg) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        File projectDir = new File(msg + "/src/test/java");
        int fileCount = projectDir.list().length;
        System.out.println("File Count:" + fileCount);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("TestSuite");
        doc.appendChild(rootElement);

        new DirExplorer((level, path, file) -> path.endsWith(".java"), new DirExplorer.FileHandler() {

            public void handle(int level, String path, File file) {
                System.out.println(path);

                try {

                    new VoidVisitorAdapter<Object>() {

                        /**
                         * visitando il file java scrive nel file test suite
                         * classe e metodo
                         *
                         * @param n contiene il nome della classe
                         * @param arg contiene il valore della classe
                         * @param methodNames contiene il nome del metodo
                         * @param caso contiene il tag TestCase
                         * @param classe contiene il nome della classe
                         * @param metodo contiene il nome del metodo
                         */
                        public void visit(ClassOrInterfaceDeclaration n, Object arg) {

                            super.visit(n, arg);

                            List<String> methodNames = new ArrayList<>();
                            VoidVisitor<List<String>> methodNameCollector = new VoidVisitorComplete.MethodNameCollector();
                            methodNameCollector.visit(n, methodNames);

                            for (String a : methodNames) {
                                Element caso = doc.createElement("TestCase");
                                String valore = Integer.toString(i);
                                caso.setAttribute("id", valore);
                                i++;
                                rootElement.appendChild(caso);
                                //  elements
                                Element classe = doc.createElement("Class");
                                //   String nome=getnome();
                                System.out.println(" * " + n.getNameAsString());
                                classe.appendChild(doc.createTextNode(n.getNameAsString()));
                                caso.appendChild(classe);

                                Element metodo = doc.createElement("method");
                                metodo.appendChild(doc.createTextNode(a));
                                caso.appendChild(metodo);
                            }
                        }
                    }.visit(JavaParser.parse(file), null);

                    System.out.println(); // empty line
                } catch (IOException e) {
                    new RuntimeException(e);
                }
            }
        }).explore(projectDir);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("testSuite.xml"));

        transformer.transform(source, result);

        System.out.println("File saved!");

    }

}
