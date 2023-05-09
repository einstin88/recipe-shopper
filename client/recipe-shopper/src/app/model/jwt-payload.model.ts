/**
 * @description Type to represent decoded JWT's payload
 */
export type JwtPayload = {
    iss: string,
    sub: string,
    exp: Date,
    iat: Date,
    scope: string
}