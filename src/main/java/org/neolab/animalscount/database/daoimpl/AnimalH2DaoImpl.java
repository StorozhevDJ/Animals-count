package org.neolab.animalscount.database.daoimpl;

import org.neolab.animalscount.database.dao.AnimalDao;
import org.neolab.animalscount.exception.AppException;
import org.neolab.animalscount.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;

public class AnimalH2DaoImpl implements AnimalDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimalH2DaoImpl.class);

    private static Connection con;

    private static AnimalH2DaoImpl instance;

    public static AnimalH2DaoImpl getInstance() throws AppException {
        if (instance == null) {
            instance = new AnimalH2DaoImpl();
        }
        return instance;
    }

    private AnimalH2DaoImpl() throws AppException {
        try {
            Class.forName("org.h2.Driver").newInstance();
            LOGGER.debug("H2 DB driver connected successful.");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("H2 DB driver NOT connected: {}", e.getMessage());
            throw new AppException(ErrorCode.DB_DRIVER_NOT_CONNECTED, e.getMessage());
        }
        try {
            con = DriverManager.getConnection("jdbc:h2:mem:database");
        } catch (SQLException throwables) {
            LOGGER.error("H2 DB NOT connected: {}", throwables.getMessage());
            throw new AppException(ErrorCode.DB_NOT_CONNECTED, throwables.getMessage());
        }
    }


    @Override
    public void importFileToTable(File file) throws AppException {
        try {
            Statement st = con.createStatement();
            st.execute("DROP TABLE IF EXISTS animal;");
            st.execute("CREATE TABLE animal AS SELECT * FROM CSVREAD('" + file.getAbsoluteFile() + "');");
            LOGGER.debug("Table 'animal' created and data from file {} to table import successful.", file.getAbsoluteFile());
        } catch (SQLException throwables) {
            LOGGER.error("Import failed: {}", throwables.getMessage());
            throw new AppException(ErrorCode.DB_IMPORT_FAILED, throwables.getMessage());
        }
    }


    @Override
    public int getCountByFiler(String filter) throws AppException {
        try {
            ResultSet result = con.createStatement().executeQuery(String.format("SELECT COUNT(*) AS cnt FROM animal WHERE %s;", filter));
            result.next();
            LOGGER.debug("Animal count = {} for filter \"{}\".", result.getInt("cnt"), filter);
            return result.getInt("cnt");
        } catch (SQLException throwables) {
            LOGGER.warn("Calculate animal counter failed: {}", throwables.getMessage());
            throw new AppException(ErrorCode.DB_GET_COUNT_FAILED, throwables.getMessage());
        }
    }
}
