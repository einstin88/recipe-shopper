export default [
  {
    context: ["/api"],
    target: "http://localhost:8080/",
  },
  {
    context: ["/auth"],
    target: "http://localhost:8090",
  },
];
