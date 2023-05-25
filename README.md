# Recipee Shopper

## Application Architechture:

The web application consists of 4 main comoponents:

1. Backend API server (Spring Boot) + MySQL
2. Authenticaion and Authorization server + MySQL + Redis caching
3. Frontend (Angular) served by NginX (not configured as reverse proxy)
4. Scrapper server (Django/Python) to gather data of supermarker products


## Deployment Setup
- All 4 components are hosted on Google Cloud Platform
- Components are deployed to a regional Kubernetes cluster with each component running in a standalone Deployment (single Container) and exposed as NodePort Service for intra-networking
- An Ingress Controller is deployed at the front of the cluster for external networking. Addtitionally, Google's Kubernetes Engine provides a global HTTP load balancer to help distribute traffic evenly to the Services
- For CI/CD, this project uses Google Cloud Build to run the pipeline. New commits on GitHub triggers the build script to create new Docker images and stored in a private Docker repository. After that, the new images will be pushed to the cluster using 'kubectl apply' based on the yaml manifests.

- Authorization server 