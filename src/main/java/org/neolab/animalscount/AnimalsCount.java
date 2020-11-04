package org.neolab.animalscount;

import org.neolab.animalscount.database.dao.AnimalDao;
import org.neolab.animalscount.database.daoimpl.AnimalH2DaoImpl;

import java.sql.*;

public class AnimalsCount {

    private static Connection getH2Connection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:database");
    }

    public static void main (String[] arg)  {

        AnimalDao animalDao = AnimalH2DaoImpl.getInstance();
        animalDao.importFileToTable("animals_list.csv");
        animalDao.getCountByFiler("weight = 'ТЯЖЕЛОЕ'");
    }
}
