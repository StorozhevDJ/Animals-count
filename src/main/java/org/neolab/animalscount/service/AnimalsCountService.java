package org.neolab.animalscount.service;

import org.neolab.animalscount.database.dao.AnimalDao;
import org.neolab.animalscount.database.daoimpl.AnimalH2DaoImpl;
import org.neolab.animalscount.exception.AppException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AnimalsCountService {
    private AnimalDao animalDao;

    public AnimalsCountService() {
        try {
            animalDao = AnimalH2DaoImpl.getInstance();
        } catch (AppException e) {
            System.err.print(e.getError() + " " + e.getMessage());
        }
    }

    public AnimalsCountService(AnimalDao animalDao) {
        this.animalDao = animalDao;
    }


    /**
     * Calculate animal count from animalsList file by filtersList file
     *
     * @param animalsList - csv file with animals properties
     * @param filtersList - files with rules for calculate (rules write as SQL syntax after "WHERE")
     */
    public void animalsCountTask(File animalsList, File filtersList) {
        try {
            animalDao.importFileToTable(animalsList);
            try (BufferedReader br = new BufferedReader(new FileReader(filtersList))) {
                for (String line; (line = br.readLine()) != null; ) {
                    int cnt = animalDao.getCountByFiler(line);
                    System.out.println("Count = " + cnt);
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        } catch (AppException e) {
            System.err.println("Error: " + e.getError().getMessage() + ". Additional message: " + e.getMessage());
        }

    }

}
