package vn.com.hoankiem360.infrastructure;

/**
 * Created by Binh on 07-Oct-17.
 */

public class BookingHotel {
    private String dateStart, dateEnd, typeRoom, name,  phone, email, number_room, number_people, idHotel, nameHotel, emailHotel, idManager;

    public BookingHotel(String dateStart, String dateEnd, String typeRoom, String name, String phone, String email, String number_room, String number_people, String idHotel, String nameHotel, String emailHotel, String idManager) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.typeRoom = typeRoom;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.number_room = number_room;
        this.number_people = number_people;
        this.idHotel = idHotel;
        this.nameHotel = nameHotel;
        this.emailHotel = emailHotel;
        this.idManager = idManager;
    }
}
