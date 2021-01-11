package model.services;

import model.dao.DaoFactory;
import model.dao.SaleDao;
import java.util.List;

import model.entities.Sale;

public class SaleService {
	
	private SaleDao dao = DaoFactory.createSaleDao();

	public List<Sale> findAll() {
		
		return dao.findAll();
	}
}