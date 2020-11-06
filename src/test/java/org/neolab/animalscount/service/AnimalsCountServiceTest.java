package org.neolab.animalscount.service;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.neolab.animalscount.database.dao.AnimalDao;
import org.neolab.animalscount.exception.AppException;

public class AnimalsCountServiceTest {

    //@TempDir
    //public Path tempDir;
    private File tempFile_animals_list, tempFile_filter_list;

    @BeforeEach
    public void before() {
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
    public void animalsCountTaskTest() {

        AnimalDao animalDao = Mockito.mock(AnimalDao.class);

        try {
            Mockito.when(animalDao.getCountByFiler("eat = 'ТРАВОЯДНОЕ'")).thenReturn(2);
            Mockito.when(animalDao.getCountByFiler("eat = 'ВСЕЯДНОЕ' AND NOT size = 'ВЫСОКОЕ'")).thenReturn(1);
        } catch (AppException e) {
            fail(e.getMessage());
        }

        AnimalsCountService animalsCountService = new AnimalsCountService(animalDao);
        animalsCountService.animalsCountTask(tempFile_animals_list, tempFile_filter_list);

        try {
            Mockito.verify(animalDao).importFileToTable(tempFile_animals_list);
            Mockito.verify(animalDao).getCountByFiler("eat = 'ТРАВОЯДНОЕ'");
            Mockito.verify(animalDao, Mockito.times(3)).getCountByFiler(Mockito.anyString());
        } catch (AppException e) {
            fail(e.getMessage());
        }
    }


}
