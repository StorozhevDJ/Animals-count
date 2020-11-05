package org.neolab.animalscount.database.dao;

import org.neolab.animalscount.exception.AppException;

import java.io.File;

public interface AnimalDao {

    void importFileToTable(File file) throws AppException;

    int getCountByFiler(String filter) throws AppException;
}
