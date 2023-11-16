package com.avsb.pdv.dto;

import com.avsb.pdv.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String username;
    private String password;
    private boolean isEneabled;

}
