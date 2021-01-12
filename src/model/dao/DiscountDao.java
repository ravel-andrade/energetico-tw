package model.dao;

import java.util.List;

import model.entities.Discount;

public interface DiscountDao {

	void insert(Discount obj);
	void update(Discount obj);
	void deleteById(Integer id);
	Discount findById(Integer id);
	List<Discount> findAll();
}