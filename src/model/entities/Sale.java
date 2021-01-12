
package model.entities;

import java.io.Serializable;


public class Sale implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer amount;
	private String name;
	private Double taxICMS;
	private Double taxIPI;
	private Double taxPIS;
	private Double taxCOFINS;
	private Product product;
	private Double total;
	private Double discount;

	private Sale Sale;

	public Sale() {
	}

	public Sale(Integer id, Integer amount, String name, Product product) {
		this.id = id;
		this.name = name;
		this.amount = amount;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getTaxICMS() {
		return taxICMS;
	}

	public void setTaxICMS(Double taxICMS) {
		this.taxICMS = taxICMS;
	}

	public Double getTaxIPI() {
		return taxIPI;
	}

	public void setTaxIPI(Double taxIPI) {
		this.taxIPI = taxIPI;
	}

	public Double getTaxPIS() {
		return taxPIS;
	}

	public void setTaxPIS(Double taxPIS) {
		this.taxPIS = taxPIS;
	}

	public Double getTaxCOFINS() {
		return taxCOFINS;
	}

	public void setTaxCOFINS(Double taxCOFINS) {
		this.taxCOFINS = taxCOFINS;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		
	}

	public Sale getSale() {
		return Sale;
	}

	public void setSale(Sale sale) {
		Sale = sale;
	}
	
	

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Sale == null) ? 0 : Sale.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((taxCOFINS == null) ? 0 : taxCOFINS.hashCode());
		result = prime * result + ((taxICMS == null) ? 0 : taxICMS.hashCode());
		result = prime * result + ((taxIPI == null) ? 0 : taxIPI.hashCode());
		result = prime * result + ((taxPIS == null) ? 0 : taxPIS.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sale other = (Sale) obj;
		if (Sale == null) {
			if (other.Sale != null)
				return false;
		} else if (!Sale.equals(other.Sale))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (taxCOFINS == null) {
			if (other.taxCOFINS != null)
				return false;
		} else if (!taxCOFINS.equals(other.taxCOFINS))
			return false;
		if (taxICMS == null) {
			if (other.taxICMS != null)
				return false;
		} else if (!taxICMS.equals(other.taxICMS))
			return false;
		if (taxIPI == null) {
			if (other.taxIPI != null)
				return false;
		} else if (!taxIPI.equals(other.taxIPI))
			return false;
		if (taxPIS == null) {
			if (other.taxPIS != null)
				return false;
		} else if (!taxPIS.equals(other.taxPIS))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sale [id=" + id + ", name=" + name + ", taxICMS=" + taxICMS + ", taxIPI=" + taxIPI + ", taxPIS="
				+ taxPIS + ", taxCOFINS=" + taxCOFINS + ", product=" + product + ", Sale=" + Sale + "]";
	}

	
}