package com.main.backend.recipeshopper.utils;

public class Urls {
    public static final String URL_PREFIX_API = "/api";
    public static final String URL_PREFIX_RECIPE = "/recipe";

    public static final String URL_PARSE_HTML = "/parse-html";
    public static final String URL_PARSE_URL = "/parse-url";
    public static final String URL_PRODUCTS = "/products";

    public static final String URL_RECIPES = "/recipes";
    public static final String URL_RECIPE_NEW = URL_PREFIX_RECIPE + "/new";
    public static final String URL_RECIPE_UPDATE = URL_PREFIX_RECIPE + "/update";
    public static final String URL_RECIPE_VIEW = URL_PREFIX_RECIPE + "/recipes";

    public static final String URL_HEALTH = "/health";

    public static final String EP_RECIPES = URL_PREFIX_API + URL_RECIPES;
    public static final String EP_RECIPE_NEW = URL_PREFIX_API + URL_RECIPE_NEW;
    public static final String EP_RECIPE_UPDATE = URL_PREFIX_API + URL_RECIPE_UPDATE;
    public static final String EP_RECIPE_VIEW = URL_PREFIX_API + URL_RECIPE_VIEW;

    public static final String EP_PARSE_HTML = URL_PREFIX_API + URL_PARSE_HTML;
    public static final String EP_PARSE_URL = URL_PREFIX_API + URL_PARSE_URL;
    public static final String EP_PRODUCTS = URL_PREFIX_API + URL_PRODUCTS;

    public static final String EP_HEALTH = URL_PREFIX_API + URL_HEALTH;
}
