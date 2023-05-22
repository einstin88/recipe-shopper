docker buildx build --build-arg SCRAPPER_SERVER_KEY=$env:SCRAPPER_SERVER_KEY -t recipee-shopping-scrapper:0.0.1 .

docker service create --name scrapper `
    -p 8000:8000 `
    --secret SCRAPPER_SERVER_KEY `
    recipee-shopping-scrapper:0.0.1