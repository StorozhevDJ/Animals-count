package org.neolab.animalscount.database.dao;

import org.neolab.animalscount.exception.AppException;

import java.io.File;

public interface AnimalDao {

	/**
	 * Load data from csv file to table 'animal' in H2 DB
	 *
	 * @param file csv with animals properties
	 * @throws AppException
	 */
	void importFileToTable(File file) throws AppException;

	/**
	 * Calculate animals counter in H2 DB by filter parameters
	 *
	 * @param filter - parameters by filter (syntaxes equivalent SQL)
	 * @return count of animals
	 * @throws AppException
	 */
	int getCountByFiler(String filter) throws AppException;
}
