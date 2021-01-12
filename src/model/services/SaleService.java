package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SaleDao;
import model.entities.Product;
import model.entities.Sale;

public class SaleService {

	private SaleDao dao = DaoFactory.createSaleDao();

	public List<Sale> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Sale obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}

	public void remove(Sale obj) {
		dao.deleteById(obj.getId());
	}
	
	public void setTaxes(Sale sale,Product product) {
		sale.setProduct(product);
		sale.setTaxICMS((product.getValue() * 0.18) *sale.getAmount());
		sale.setTaxIPI((product.getValue() * 0.04) *sale.getAmount());
		sale.setTaxPIS((product.getValue() * 0.0186) *sale.getAmount());
		sale.setTaxCOFINS((product.getValue() * 0.0854) *sale.getAmount());
		sale.setTotal(getTotal(sale, product));
	}
	
	public Double getTotal(Sale sale,Product product) {
		
		Double total = (
				sale.getTaxCOFINS()
				+sale.getTaxICMS()
				+sale.getTaxIPI()
				+sale.getTaxPIS()
				+(product.getValue()* sale.getAmount() )
				);
		
		return total;
	}
}