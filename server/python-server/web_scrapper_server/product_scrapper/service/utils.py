def price_in_float(price_in_str: str):
    return float(price_in_str[1:])


def get_quantity_and_unit(parsed_string: str):
    return parsed_string.split(' ')
