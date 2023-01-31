package com.izzydrive.backend.utils;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.LocationDTO;

public class AddressesUtil {

    public static AddressOnMapDTO getBanijskaAddress() {
        return new AddressOnMapDTO( 19.8000267,45.2421319, "32, Банијска, Телеп, МЗ Братство телеп, Нови Сад");
    }

    public static AddressOnMapDTO getZeleznickaAddress() {
        return new AddressOnMapDTO(19.8294979,45.2647343, "Железничка станица, Булевар Јаше Томића, Банатић, МЗ Омалдински покрет, Нови Сад");
    }

    public static AddressOnMapDTO getPromenadaAddress() {
        return new AddressOnMapDTO(19.843409188971894, 45.245625649999994, "Променада, 119, Булевар ослобођења, МЗ Бошко Буха, МЗ Прва војвођанска бригада, Нови Сад, Град Нови Сад, Јужнобачки управни округ, Војводина, 21000, Србија");
    }

    public static AddressOnMapDTO getCvecaraDraganaBulevarCaraLazaraAddress() {
        return new AddressOnMapDTO(19.8259713, 45.2398416, "Cvećara Dragana S, 85, Булевар Цара Лазара, МЗ Иво Андрић, Лиман, Нови Сад, Град Нови Сад, Јужнобачки управни округ, Војводина, 21102, Србија");
    }

    public static AddressOnMapDTO getBulevarPatrijarhaPavla11Address() {
        return new AddressOnMapDTO(19.819700, 45.239630, "Bulevar Patrijarha Pavla 11");
    }

    public static AddressOnMapDTO getMaksimaGorkog11Address() {
        return new AddressOnMapDTO(19.8467534, 45.2500965, "11, Максима Горког, Стари град, МЗ Прва војвођанска бригада, Нови Сад, Град Нови Сад, Јужнобачки управни округ, Војводина, 21101, Србија");
    }

    public static AddressOnMapDTO getLocationOutsideOfNoviSad() {
        return new AddressOnMapDTO(19.9569769, 45.2980289, "Јована Јовановића Змаја, МЗ Каћ, Каћ, Град Нови Сад, Јужнобачки управни округ, Војводина, 21241, Србија");
    }

    public static LocationDTO getBanijskaLocation() {
        return new LocationDTO(19.8000267,45.2421319);
    }

    public static AddressOnMapDTO getBulevarPatrijarhaPavla11Adress() {
        return new AddressOnMapDTO(19.8197137,45.2396127, "11, Булевар патријарха Павла, Телеп, МЗ Јужни телеп, Нови Сад, Град Нови Сад, Јужнобачки управни округ, Војводина, 21102, Србија");
    }

    public static AddressOnMapDTO getBulevarPatrijarhaPavla5Address() {
        return new AddressOnMapDTO(19.8204828,45.2394661, "5, Булевар патријарха Павла, Телеп, МЗ Јужни телеп, Нови Сад, Град Нови Сад, Јужнобачки управни округ, Војводина, 21102, Србија");
    }
}
