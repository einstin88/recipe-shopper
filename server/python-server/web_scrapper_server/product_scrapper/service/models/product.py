'''
Class to model product objects
'''
class Product:
    def __init__(self,
                 name: str,
                 url: str,
                 pack_size: int,
                 #  pack_size_unit: str,
                 price: float,
                 img: str) -> None:

        self.name = name
        self.url = url
        self.pack_size = pack_size
        # self.pack_size_unit = pack_size_unit
        self.price = price
        self.img = img

    def __str__(self) -> str:
        return f'[Product] -> name={self.name} | pack={self.pack_size} | price={self.price}'
