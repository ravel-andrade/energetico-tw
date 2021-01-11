package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Sale;

public class SaleService {

	public List<Sale> findAll() {
		List<Sale> list = new ArrayList<>();
		list.add(new Sale(1, "Books"));
		list.add(new Sale(2, "Computers"));
		list.add(new Sale(3, "Electronics"));
		return list;
	}
}