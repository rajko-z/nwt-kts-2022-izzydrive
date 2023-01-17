package com.izzydrive.backend.dto;
import com.izzydrive.backend.model.Address;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressDTO {
    private Long id;
    private String state;
    private String city;
    private String street;

    private String name;

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.state = address.getState();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.name = address.getName();
    }
}
