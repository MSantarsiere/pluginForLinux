/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.plug.dado;

import java.io.File;

/**
 * permette di esplorare la Directory del progetto
 * per cercare i file che terminano con .java
 * @author Rembor
 */
public class DirExplorer {

    public interface FileHandler {

        void handle(int level, String path, File file);
    }

    public interface Filter {

        boolean interested(int level, String path, File file);
    }

    private FileHandler fileHandler;
    private Filter filter;

    public DirExplorer(Filter filter, FileHandler fileHandler) {
        this.filter = filter;
        this.fileHandler = fileHandler;
    }

    public void explore(File root) {
        explore(0, "", root);
    }
/**
 *Esplora tutta la directory in cerca dei file che finiscono in .java
 * @param level contiene il valore della directory
 * @param path contiene la path della src del progetto
 * @param file contiene il file trovato
 */
    private void explore(int level, String path, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                explore(level + 1, path + "/" + child.getName(), child);
            }
        } else {
            if (filter.interested(level, path, file)) {
                fileHandler.handle(level, path, file);
            }
        }
    }

}
