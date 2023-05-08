/**
 * @description Type respresenting credentials format to use for user authentication
 */
export type AuthPayLoad = {
  username: string;
  password: string;
  csrf?: string;
};
