package com.github.caac.demo;

import java.util.List;

public class CustomerDto {

    private Long id;

    private String name;

    private int age;

    private String email;

    private List<AddressDto> addresses;

    public CustomerDto(Long id, String name, int age, String email, List<AddressDto> addresses) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.addresses = addresses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        this.addresses = addresses;
    }
}
