package com.izzydrive.backend.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="images")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false, unique = true)
    private String name;

    public Image(String photoName) {
        this.name = photoName;
    }
}
