package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SaleDao;
import model.entities.*;
import model.services.DiscountService;

public class SaleService {

	private SaleDao dao = DaoFactory.createSaleDao();
	private DiscountService discountService;

	
	public void setDiscountService(DiscountService service) {
		discountService = service;
	}
	
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
		setDiscountService(new DiscountService());
		sale.setProduct(product);
		sale.setTaxICMS((product.getValue() * 0.18) *sale.getAmount());
		sale.setTaxIPI((product.getValue() * 0.04) *sale.getAmount());
		sale.setTaxPIS((product.getValue() * 0.0186) *sale.getAmount());
		sale.setTaxCOFINS((product.getValue() * 0.0854) *sale.getAmount());
		sale.setTotal(getTotal(sale, product));
		discountService.setDiscount(sale);
		
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