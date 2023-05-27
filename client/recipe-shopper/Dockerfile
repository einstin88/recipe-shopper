FROM node:slim AS builder

WORKDIR /app

COPY . .

# Install app's dependencies
# RUN npm install -g npm@latest
RUN npm i -g @angular/cli
RUN npm i --legacy-peer-deps

RUN ng build

# Stage 2: setup reverse-proxy server to serve app behind external load balancer
FROM nginx:stable-alpine

COPY --from=builder /app/dist/recipe-shopper /usr/share/nginx/html

# Adapt nginx config to suit production k8s's DNS
#  http://nginx.org/en/docs/http/server_names.html
COPY nginx-deployment.conf /etc/nginx/conf.d

# For testing on localhost
# COPY default.conf /etc/nginx/conf.d
# COPY nginx.conf /etc/nginx

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
