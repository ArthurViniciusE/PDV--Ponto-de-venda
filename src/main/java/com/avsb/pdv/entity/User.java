package com.avsb.pdv.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "Campo nome é obrigatório!")
    private String name;

    @Column(length = 30, nullable = false)
    @NotBlank(message = "O campo username é obrigatório")
    private String username;

    @Column(length = 30, nullable = false)
    @NotBlank(message = "O campo senha é obrigatório")
    private String password;


    private boolean isEneabled;

    @OneToMany(mappedBy = "user")
    private List<Sale> sales;

}
