FROM node:18.16.0-slim AS builder

WORKDIR /app

COPY . .
RUN ls

# Install app's dependencies
RUN npm install -g npm@latest
RUN ["npm", "i", "-g", "@angular/cli"]
RUN ["npm", "i"]

RUN ["ng", "build"]

# Stage 2: setup reverse-proxy server to serve app behind external load balancer
FROM nginx:stable-alpine

COPY --from=builder /app/dist/recipe-shopper /usr/share/nginx/html
COPY nginx.conf /etc/nginx/sites-available/default

CMD ["nginx" "-g" "daemon off;"]