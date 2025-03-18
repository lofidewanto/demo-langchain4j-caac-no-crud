package com.github.caac.demo;

import java.util.List;

public record CustomerDto(Long id, String name, int age, 
    String email, List<AddressDto> addresses) {
}
