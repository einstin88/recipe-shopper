FROM node:18.16.0-slim AS builder

WORKDIR /app

COPY . ./

RUN ["npm", "i", "-g", "@angular/cli"]
RUN ["npm", "i"]

RUN ["ng", "build"]

# Stage 2: setup reverse-proxy server to serve app
FROM nginx:stable-alpine

COPY --from=builder /app/dist/recipe-shopper /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/
