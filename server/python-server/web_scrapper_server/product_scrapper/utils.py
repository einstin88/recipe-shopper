
def create_json_response(category: str, products: list[dict]):
    return {
        "category": category,
        "resultSize": len(products),
        "results": products
    }
