package model.dao;
import db.DB;
import model.dao.impl.SaleDaoJDBC;
import model.dao.impl.ProductDaoJDBC;
import model.dao.impl.DiscountDaoJDBC;




	public class DaoFactory {

		public static SaleDao createSaleDao() {
			return new SaleDaoJDBC(DB.getConnection());
		}
		
		public static ProductDao createProductDao() {
			return new ProductDaoJDBC(DB.getConnection());
		}
		
		public static DiscountDao createDiscountDao() {
			return new DiscountDaoJDBC(DB.getConnection());
		}
		
		
	}


