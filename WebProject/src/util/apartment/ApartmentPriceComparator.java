package util.apartment;

import java.util.Comparator;

import beans.Apartment;

public class ApartmentPriceComparator implements Comparator<Apartment>{
	
	private Order order;
	
	public ApartmentPriceComparator(Order order) {
		// TODO Auto-generated constructor stub
		this.order = order;
	}
	@Override
	public int compare(Apartment a1, Apartment a2) {
		// TODO Auto-generated method stub
		Double diffAsc = a1.getPrice() - a2.getPrice();
		Double diffDesc = a2.getPrice() - a1.getPrice();
		if (this.order == Order.ASC)
			return diffAsc.intValue();
		return diffDesc.intValue();
	}

}
