package model.services;

import model.dao.DaoFactory;
import model.dao.ProductDao;
import java.util.List;

import model.entities.Product;

public class ProductService {
	
	private ProductDao dao = DaoFactory.createProductDao();

	public List<Product> findAll() {
		
		return dao.findAll();
	}
}