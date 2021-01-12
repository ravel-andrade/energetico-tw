package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.entities.Sale;
import model.dao.SaleDao;

public class SaleDaoJDBC implements SaleDao {

	private Connection conn;
	
	public SaleDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Sale findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM sale WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Sale obj = new Sale();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setTaxCOFINS(rs.getDouble("TaxCOFINS"));
				obj.setTaxIPI(rs.getDouble("TaxIPI"));
				obj.setTaxPIS(rs.getDouble("TaxPIS"));
				obj.setTaxICMS(rs.getDouble("TaxICMS"));
				obj.setTotal(rs.getDouble("Total"));
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Sale> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Sale ORDER BY Name");
			rs = st.executeQuery();

			List<Sale> list = new ArrayList<>();

			while (rs.next()) {
				Sale obj = new Sale();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setTaxCOFINS(rs.getDouble("TaxCOFINS"));
				obj.setTaxIPI(rs.getDouble("TaxIPI"));
				obj.setTaxPIS(rs.getDouble("TaxPIS"));
				obj.setTaxICMS(rs.getDouble("TaxICMS"));
				obj.setTotal(rs.getDouble("Total"));
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void insert(Sale obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO Sale " +
				"(Name, TaxCOFINS, TaxIPI, TaxPIS, TaxICMS, Total) " +
				"VALUES " +
				"(?, ?, ?, ?, ?, ?)", 
				Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setDouble(2, obj.getTaxCOFINS());
			st.setDouble(3, obj.getTaxIPI());
			st.setDouble(4, obj.getTaxPIS());
			st.setDouble(6, obj.getTotal());
			st.setDouble(5, obj.getTaxICMS());


			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Sale obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Sale " +
							"SET Name = ?, TaxCOFINS = ?, TaxIPI = ?, TaxPIS = ? , TaxICMS = ?, Total = ? " +
							"WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setDouble(2, obj.getTaxCOFINS());
			st.setDouble(3, obj.getTaxIPI());
			st.setDouble(4, obj.getTaxPIS());
			st.setDouble(5, obj.getTaxICMS());
			st.setDouble(6, obj.getTotal());
			st.setInt(7, obj.getId());

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM Sale WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
}