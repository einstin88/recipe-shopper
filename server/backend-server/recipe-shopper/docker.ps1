docker buildx build -t recipee-shopping-backend:0.0.1 .

docker service create --name backend `
    -p 8080:8080 `
    --secret MYSQLUSER `
    --secret MYSQLPASSWORD `
    --secret MYSQL_URL_BACKEND `
    --secret SCRAPPER_URL `
    --secret KEY_URL `
    --secret GOOGLE_CREDENTIALS `
    --secret GOOGLE_APPLICATION_CREDENTIALS `
    recipee-shopping-backend:0.0.1