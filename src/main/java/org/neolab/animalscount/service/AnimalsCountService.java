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

    public void animalsCountTask(File animalsList, File filtersList) {
        try {
            animalDao.importFileToTable(animalsList);
            try (BufferedReader br = new BufferedReader(new FileReader(filtersList))) {
                for (String line; (line = br.readLine()) != null; ) {
                    int cnt = animalDao.getCountByFiler(line);
                    System.out.println("Count = " + cnt);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (AppException e) {
            System.err.println("Error: " + e.getError().getMessage() + ". Additional message: " + e.getMessage());
        }

    }

}
