package util.apartment;

import java.util.Comparator;

import beans.Apartment;

public class ApartmentGuestsComparator implements Comparator<Apartment>{

	private Order order;
	
	public ApartmentGuestsComparator(Order order) {
		// TODO Auto-generated constructor stub
		this.order = order;
	}
	
	@Override
	public int compare(Apartment a1, Apartment a2) {
		// TODO Auto-generated method stub
		if (this.order == Order.ASC)
			return a1.getGuests() - a2.getGuests();
		return a2.getGuests() - a1.getGuests();
	}
}
