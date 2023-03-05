package com.example.demo;

import com.example.demo.model.Address;
import com.example.demo.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

    public static List<Address> ADDRESS_LIST = List.of(new Address("street 1"), new Address("street 2"));
    public static List<Client> CLIENT_LIST = List.of(new Client("client 1"), new Client("client 2"));
    @GetMapping("/api/address")
    public Page<Address> listAddress() {
        return new PageImpl<>(ADDRESS_LIST);
    }

    @GetMapping("/api/client")
    public Page<Client> listClient() {
        return new PageImpl<>(CLIENT_LIST);
    }

}
