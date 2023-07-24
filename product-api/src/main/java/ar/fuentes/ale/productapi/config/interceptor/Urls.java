package ar.fuentes.ale.productapi.config.interceptor;

import java.util.List;

public class Urls {

    public static final List<String> PROTECTED_URLS = List.of(
            "api/category",
            "api/supplier",
            "api/product"
    );
}
