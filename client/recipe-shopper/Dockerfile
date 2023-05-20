FROM node:18.16.0-slim AS builder

WORKDIR /app

COPY . .
RUN ls

# Install app's dependencies
RUN npm install -g npm@9.6.6
RUN ["npm", "i", "-g", "@angular/cli"]
RUN ["npm", "i"]

RUN ["ng", "build"]

# Stage 2: setup reverse-proxy server to serve app behind external load balancer
FROM nginx:stable-alpine

COPY --from=builder /app/dist/recipe-shopper /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/
