# Recipee Shopper

## Objective

The idea of this application is to shop for groceries more directly based on recipes and make the shopping easier for people who seldom cook.

## Features

1. Create recipes with your procedures and add ingredients that are gathered from supermarket website directly
2. Recipes can be updated by the creator
3. Other users can browse these recipes and add the ingredients they need to their cart
4. For now, a summary email will be sent to users when they checkout their cart. The email contains links to the recipes from their cart so that referring to the procedures is direct. There will also be links to the products on the supermarket website where users can use to purchase (payment feature and integration with supermarkets are not implemented yet for this application)

## Security of user data

- User passwords are BCrypt encoded on the database
- User sessions are managed with JWS signed with RS256 private keys and authenticated with a public keys on the backend
- Core database uses OAuth2 and HTTPS for external connection
- Email sent to user does not contain images or scripts to minimise risk of malicious code execution
- Email is sent via a registered business account on Google Workspace

## Application Architechture:

The web application consists of 4 main comoponents:

1. Backend REST API server (Spring Boot) + MySQL
   > for serving application data
2. Authentication and Authorization server + MySQL + Redis caching
   > for user management/authentication/authorization
3. Frontend (Angular) served by NginX (not configured as reverse proxy)
   > for hosting SPA and depended resources
4. Scrapper server (Django/Python)
   > for gathering product data from supermarket

## Deployment Setup

- All 4 components are hosted on Google Cloud Platform
- Scrapper server is deployed on Cloud Run to improve stability on the K8s cluster
- The other components are deployed to Google Kubernetes Engine (GKE) with each component running in resepective Deployment and exposed as NodePort Service for intra-networking
- An Ingress Controller is configured at the front of the cluster for external networking. Addtitionally, GKE provides their global HTTP load balancer to help distribute traffic evenly to the Services
- For CI/CD, this project uses Google Cloud Build to run the pipeline. New commits on GitHub triggers the build script to create new Docker images that will be stored in a private Docker repository. The new images will then be pushed to the cluster based on the yaml manifests.
- HTTP requests to all 3 servers (including scrapper server) require JWS or OAuth2 authorization
