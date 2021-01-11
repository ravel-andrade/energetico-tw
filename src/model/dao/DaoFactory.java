package model.dao;
import db.DB;
import model.dao.impl.SaleDaoJDBC;
import model.dao.impl.ProductDaoJDBC;




	public class DaoFactory {

		public static SaleDao createSaleDao() {
			return new SaleDaoJDBC(DB.getConnection());
		}
		
		public static ProductDao createProductDao() {
			return new ProductDaoJDBC(DB.getConnection());
		}
		
		
	}


