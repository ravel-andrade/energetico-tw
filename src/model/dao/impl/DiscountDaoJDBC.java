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
import model.entities.Discount;
import model.dao.DiscountDao;

public class DiscountDaoJDBC implements DiscountDao {

	private Connection conn;
	
	public DiscountDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Discount findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM discount WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Discount obj = new Discount();
				obj.setId(rs.getInt("Id"));
				obj.setValue(rs.getDouble("Value"));
				obj.setAmount(rs.getInt("Amount"));
				
				
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
	public List<Discount> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM discount ORDER BY Value");
			rs = st.executeQuery();

			List<Discount> list = new ArrayList<>();

			while (rs.next()) {
				Discount obj = new Discount();
				obj.setId(rs.getInt("Id"));
				obj.setValue(rs.getDouble("Value"));
				obj.setAmount(rs.getInt("Amount"));
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
	public void insert(Discount obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO discount " +
				"(Amount, Value) " +
				"VALUES " +
				"(?, ?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setDouble(1, obj.getAmount());
			st.setDouble(2, obj.getValue());
			
			
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
	public void update(Discount obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE discount " +
							"SET Value = ?, Value = ? " +
							"WHERE Id = ?");

			st.setDouble(1, obj.getValue());
			st.setDouble(2, obj.getValue());
			st.setInt(3, obj.getId());

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
				"DELETE FROM Discount WHERE Id = ?");

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