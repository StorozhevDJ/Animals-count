package org.neolab.animalscount.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neolab.animalscount.database.dao.AnimalDao;
import org.neolab.animalscount.database.daoimpl.AnimalH2DaoImpl;
import org.neolab.animalscount.exception.AppException;
import org.neolab.animalscount.exception.ErrorCode;

public class AnimalsH2DaoImplTest {

    private static AnimalDao animalDao;

    private static File tempFile_animals_list, tempFile_filter_list;

    @BeforeAll
    public static void beforeAll() {
        try {
            animalDao = AnimalH2DaoImpl.getInstance();
        } catch (AppException e) {
            fail(e.getMessage());
        }

        try {
            tempFile_animals_list = File.createTempFile("animals_list", ".csv");
            tempFile_filter_list = File.createTempFile("filter_list", ".afl");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        try (FileWriter writer = new FileWriter(tempFile_animals_list)) {
            List<String> lines = Arrays.asList("weight,size,eat", "ЛЕГКОЕ,МАЛЕНЬКОЕ,ВСЕЯДНОЕ",
                    "ТЯЖЕЛОЕ,МАЛЕНЬКОЕ,ТРАВОЯДНОЕ", "ТЯЖЕЛОЕ,НЕВЫСОКОЕ,ТРАВОЯДНОЕ");
            for (String line : lines) {
                writer.write(line);
                writer.write(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        try (FileWriter writer = new FileWriter(tempFile_filter_list)) {
            List<String> lines = Arrays.asList("eat = 'ТРАВОЯДНОЕ'",
                    "(eat = 'ТРАВОЯДНОЕ' OR eat = 'ПЛОТОЯДНОЕ') AND size = 'МАЛЕНЬКОЕ'",
                    "eat = 'ВСЕЯДНОЕ' AND NOT size = 'ВЫСОКОЕ'");
            for (String line : lines) {
                writer.write(line);
                writer.write(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getInstanceTest() {
        try {
            assertEquals(animalDao, AnimalH2DaoImpl.getInstance());
        } catch (AppException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void importFileToTableTest() {
        try {
            animalDao.importFileToTable(tempFile_animals_list);
            animalDao.importFileToTable(tempFile_animals_list);
        } catch (AppException e) {
            fail(e.getMessage());
        }

        try {
            animalDao.importFileToTable(new File("error_file"));
            fail();
        } catch (AppException e) {
            assertEquals(ErrorCode.DB_IMPORT_FAILED, e.getError());
        }
    }

    @Test
    public void getCountByFilerTest() {
        try {
            animalDao.getCountByFiler("eat = 'ТРАВОЯДНОЕ'");
            fail();
        } catch (AppException e) {
            assertEquals(ErrorCode.DB_GET_COUNT_FAILED, e.getError());
        }

        try {
            animalDao.importFileToTable(tempFile_animals_list);
            animalDao.importFileToTable(tempFile_animals_list);
            assertEquals(2, animalDao.getCountByFiler("eat = 'ТРАВОЯДНОЕ'"));
            assertEquals(1, animalDao.getCountByFiler("eat = 'ВСЕЯДНОЕ'"));
            assertEquals(0, animalDao.getCountByFiler("size = 'ВСЕЯДНОЕ'"));
        } catch (AppException e) {
            fail(e.getMessage());
        }

        try {
            assertEquals(0, animalDao.getCountByFiler("error = 'ВСЕЯДНОЕ'"));
            fail();
        } catch (AppException e) {
            assertEquals(ErrorCode.DB_GET_COUNT_FAILED, e.getError());
        }
    }

}