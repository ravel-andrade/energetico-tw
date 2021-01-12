package model.services;

import model.dao.DaoFactory;
import model.dao.DiscountDao;
import java.util.List;

import model.entities.Discount;
import model.entities.Sale;

public class DiscountService {
	
	public void setDiscount(Sale sale) {
		List<Discount> list;
		Double value = 0.0;
		list = findAll();
		for(Discount item: list){
            if(sale.getAmount()>=item.getAmount()) {
            	value = item.getValue();
            }
        }
		if(value != 0.0) {	
			sale.setTotal(sale.getTotal()-value);
		}
		
		sale.setDiscount(value);
		
	}
	
	private DiscountDao dao = DaoFactory.createDiscountDao();

	public List<Discount> findAll() {
		
		return dao.findAll();
	}
	
	public void saveOrUpdate(Discount obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Discount obj) {
		dao.deleteById(obj.getId());
	}
}