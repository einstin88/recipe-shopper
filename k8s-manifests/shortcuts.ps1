# View nodes and the external IPs
kubectl get nodes --output wide

# View component manifests/configurations
kubectl get service server-frontend-1-service --output yaml -n backend

# List pods
kubectl get pods -A

# Get shell into pods for troubleshooting
kubectl -n backend exec -it server-auth-86f8c66795-9ft7d -- sh
