docker buildx build -t recipee-shopping-scrapper:0.0.1 .

docker service create --name scrapper `
    -p 8000:8000 `
    --secret SCRAPPER_SERVER_KEY `
    recipee-shopping-scrapper:0.0.1