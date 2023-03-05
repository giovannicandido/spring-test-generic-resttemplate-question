package com.example.demo;

import com.example.demo.model.Address;
import com.example.demo.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApiControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void listAddress() {


        ParameterizedTypeReference<CustomPageImpl<Address>> typeReference = new ParameterizedTypeReference<>() {
        };

        final var list = testRestTemplate.exchange("/api/address",
                HttpMethod.GET, null, typeReference);

        assertThat(list.getBody().getContent()).containsExactlyInAnyOrderElementsOf(ApiController.ADDRESS_LIST);

    }

    @Test
    void listClient() {
        ParameterizedTypeReference<CustomPageImpl<Client>> typeReference = new ParameterizedTypeReference<>() {
        };

        final var list = testRestTemplate.exchange("/api/client",
                HttpMethod.GET, null, typeReference);

        assertThat(list.getBody().getContent()).containsExactlyInAnyOrderElementsOf(ApiController.CLIENT_LIST);

    }

    @ParameterizedTest
    @MethodSource("testFactory")
    void listWithClassType(String type, String api, List expected) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName(type);

        CustomPageImpl<?> customPage = new CustomPageImpl(List.of(aClass.newInstance()));
        ParameterizedTypeReference<Page<?>> typeRef = ParameterizedTypeReference.forType(customPage.getClass());

        final var list = testRestTemplate.exchange(api, HttpMethod.GET, null, typeRef);

        assertThat(list.getBody().getContent()).containsExactlyInAnyOrderElementsOf(expected);


    }

    private static Stream<Arguments> testFactory() {
        return Stream.of(
                Arguments.of("com.example.demo.model.Address", "/api/address", ApiController.ADDRESS_LIST),
                Arguments.of("com.example.demo.model.Client", "/api/client", ApiController.CLIENT_LIST)
        );
    }
}