/**
 * @description Type representing the details of the products that can be added to the recipes
 */
export type Product = {
    productId: string,
    name: string,
    url: string,
    pack_size: string,
    price: number,
    img: string
    timeStamp: Date
}