package com.cafe.demo.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

    UserWrapper user = new com.cafe.demo.wrapper.UserWrapper(1,"Li","123@gmail.com","1234567890","false");

    private Integer id;

    private String name;

    private String email;

    private String contactNumer;

    private String status;

    public UserWrapper(Integer id, String name, String email, String contactNumer, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contactNumer = contactNumer;
        this.status = status;
    }

}
