package util.apartment;

import java.util.Comparator;

import beans.Apartment;

public class ApartmentHostComparator implements Comparator<Apartment>{

	private Order order;
	
	public ApartmentHostComparator(Order order) {
		// TODO Auto-generated constructor stub
		this.order = order;
	}
	
	@Override
	public int compare(Apartment a1, Apartment a2) {
		// TODO Auto-generated method stub
		if (this.order == Order.ASC)
			return a1.getHostId().compareTo(a2.getHostId());
		return a2.getHostId().compareTo(a1.getHostId());
	}

}
