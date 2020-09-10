package util.apartment;

import java.util.Comparator;

import beans.Apartment;

public class ApartmentTypeComparator implements Comparator<Apartment> {

	private Order order;
	
	public ApartmentTypeComparator(Order order) {
		// TODO Auto-generated constructor stub
		this.order = order;
	}
	
	@Override
	public int compare(Apartment a1, Apartment a2) {
		// TODO Auto-generated method stub
		if (this.order == Order.ASC)
			return a1.getApartmentType().compareTo(a2.getApartmentType());
		return a2.getApartmentType().compareTo(a1.getApartmentType());
	}
}
