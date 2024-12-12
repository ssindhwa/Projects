chmod +x launch.sh

#!/bin/bash

# Start Minikube with Docker as the driver
minikube --driver=docker start

# Set up Minikube Docker environment
eval $(minikube docker-env)

# Build and deploy user-service
cd userService
docker build -t anushka-user-service:v0 .
kubectl create deployment anushka-userservice --image=anushka-user-service:v0
kubectl expose deployment anushka-userservice --type=LoadBalancer --port=8080
cd ..

# Build and deploy H2DB
cd H2DB
docker build -t anushka-h2db:v3 .
kubectl create deployment anushka-dbservice --image=anushka-h2db:v3
kubectl expose deployment anushka-dbservice --type=ClusterIP --port=9082
#kubectl apply -f h2db-service.yaml
#kubectl apply -f h2db-deployment.yaml
cd ..

# Build and deploy wallet-service
cd walletService
docker build -t sakshi-wallet-service:v2 .
kubectl create deployment sakshi-walletservice --image=sakshi-wallet-service:v2 
kubectl expose deployment sakshi-walletservice --type=LoadBalancer --port=8082
cd ..

# Build and deploy booking-service
cd bookingService
docker build -t sakshi-booking-service:v1 .
kubectl create deployment sakshi-bookingservice --image=sakshi-booking-service:v1 
kubectl expose deployment sakshi-bookingservice --type=LoadBalancer --port=8081
kubectl apply -f hpa.yaml
cd ..

pkill -f port-forward
sleep 20s
# Forward ports for services
kubectl port-forward service/anushka-userservice 8080:8080 &
kubectl port-forward service/sakshi-walletservice 8082:8082 &
kubectl port-forward service/sakshi-bookingservice 8081:8081 &
#gnome-terminal -- minikube tunnel

# Start Minikube tunnel for LoadBalancer services
#minikube tunnel




