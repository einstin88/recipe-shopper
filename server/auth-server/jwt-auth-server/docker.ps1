docker buildx build -t recipee-shopping-auth:0.0.1 .

docker service create --name auth `
-p 8090:8080 `
--secret REDISHOST `
--secret REDISPORT `
--secret REDISUSER `
--secret REDISPASSWORD `
--secret MYSQLUSER `
--secret MYSQLPASSWORD `
--secret MYSQL_URL_JDBC `
recipee-shopping-auth:0.0.1