package org.neolab.animalscount.database.daoimpl;

import org.neolab.animalscount.database.dao.AnimalDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AnimalH2DaoImpl implements AnimalDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimalH2DaoImpl.class);

    private static Connection con;

    private static AnimalH2DaoImpl instance;

    public static AnimalH2DaoImpl getInstance() {
        if (instance == null) {
            instance = new AnimalH2DaoImpl();
        }
        return instance;
    }

    private AnimalH2DaoImpl() {
        try {
            Class.forName("org.h2.Driver").newInstance();
            LOGGER.debug("H2 DB driver connected successful.");
        } catch (InstantiationException e) {
            LOGGER.error("H2 DB driver NOT connected: {}", e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.error("H2 DB driver NOT connected: {}", e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.error("H2 DB driver NOT connected: {}", e.getMessage());
        }
        try {
            con = DriverManager.getConnection("jdbc:h2:mem:database");
        } catch (SQLException throwables) {
            LOGGER.error("H2 DB NOT connected: {}", throwables.getMessage());
        }

    }


    @Override
    public void importFileToTable(String path) {
        try {
            Statement st = con.createStatement();
            st.execute("CREATE TABLE animal AS SELECT * FROM CSVREAD('" + path + "');");
            LOGGER.debug("Table 'animal' created, data to table import successful.");
            ResultSet result = st.executeQuery("SELECT * FROM ANIMAL");
            while (result.next()) {
                String name = result.getString("weight");
                System.out.println(result.getString("size") + " " + name);
            }
            LOGGER.debug("H2 DB import data from file {} in table successful completed.", path);
        } catch (SQLException throwables) {
            LOGGER.error("Import failed: {}", throwables.getMessage());
        }
    }

    @Override
    public int getCountByFiler(String filter){
        try {
            ResultSet result = con.createStatement().executeQuery("SELECT COUNT(*) AS cnt FROM animal WHERE " + filter);
            result.next();
            LOGGER.debug("Animal count {} for filter \"{}\".", result.getInt("cnt"), filter);
            return result.getInt("cnt");
        } catch (SQLException throwables) {
            LOGGER.error("Calculate animal counter failed: {}", throwables.getMessage());
            return 0;
        }
    }
}
