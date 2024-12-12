chmod +x teardown.sh

kubectl delete service anushka-userservice
kubectl delete service sakshi-bookingservice
kubectl delete service sakshi-walletservice
kubectl delete service anushka-dbservice

kubectl delete deployment anushka-userservice
kubectl delete deployment sakshi-bookingservice
kubectl delete deployment sakshi-walletservice
kubectl delete deployment anushka-dbservice

docker container prune -f
docker rmi anushka-user-service:v0
docker rmi anushka-h2db:v3
docker rmi sakshi-wallet-service:v2
docker rmi sakshi-booking-service:v1
minikube stop