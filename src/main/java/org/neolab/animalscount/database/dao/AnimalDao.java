package org.neolab.animalscount.database.dao;

public interface AnimalDao {

    void importFileToTable(String path);
    int getCountByFiler(String filter);
}
