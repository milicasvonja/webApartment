package util.apartment;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import beans.Apartment;

public class ApartmentDateComparator implements Comparator<Apartment>{

	private Order order;
	
	public ApartmentDateComparator(Order order) {
		// TODO Auto-generated constructor stub
		this.order = order;
	}
	
	@Override
	public int compare(Apartment a1, Apartment a2) {
		// TODO Auto-generated method stub
		
		Date apartment1MinDate = Collections.min(a1.getDates());
		Date apartment2MinDate = Collections.min(a2.getDates());
		if (order == Order.ASC)
			return apartment1MinDate.compareTo(apartment2MinDate);
		return apartment2MinDate.compareTo(apartment1MinDate);
	}

}
