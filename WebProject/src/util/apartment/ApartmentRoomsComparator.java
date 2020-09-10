package util.apartment;

import java.util.Comparator;

import beans.Apartment;

public class ApartmentRoomsComparator implements Comparator<Apartment>{
	
	private Order order;
	
	public ApartmentRoomsComparator(Order order) {
		// TODO Auto-generated constructor stub
		this.order = order;
	}
	
	@Override
	public int compare(Apartment a1, Apartment a2) {
		// TODO Auto-generated method stub
		if (this.order == Order.ASC)
			return a1.getRooms() - a2.getRooms();
		return a2.getRooms() - a1.getRooms();
	}

}
