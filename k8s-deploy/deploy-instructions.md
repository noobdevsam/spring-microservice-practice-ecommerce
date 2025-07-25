## Required Images:

- `bitnami/zipkin:latest`
- `binami/kafka:latest`
- `keycloak/keycloak:latest`
- `maildev/maildev:latest`
- `mongo:latest`
- `mysql:latest`

From branch `40-dockerize-microservices` with `mvn clean package -DskipTests spring-boot:build-image` from each
microservice :

- `config-server-service:0.0.1-SNAPSHOT`
- `config-discovery-service:0.0.1-SNAPSHOT`
- `customer-service:0.0.1-SNAPSHOT`
- `product-service:0.0.1-SNAPSHOT`
- `order-service:0.0.1-SNAPSHOT`
- `payment-service:0.0.1-SNAPSHOT`
- `notification-service:0.0.1-SNAPSHOT`
- `gateway-service:0.0.1-SNAPSHOT`

## Deployment Instructions

1. Ensure you have the required images available in your local Docker registry or a remote registry.
2. Make sure you have a Kubernetes cluster running and `kubectl` configured to interact with it.

For mongodb:

```bash
kubectl create deployment ms-db-mongo --image=mongo:latest --dry-run=client -o yaml > mongo-deployment.yaml
# edit mongo-deployment.yaml to set the correct environment variables and other configurations
kubectl apply -f mongo-deployment.yaml

kubectl create service clusterip ms-db-mongo --tcp=27017:27017 --dry-run=client -o yaml > mongo-service.yaml
kubectl apply -f mongo-service.yaml
```

For zipkin:

```bash
kubectl create deployment ms-zipkin --image=bitnami/zipkin:latest --dry-run=client -o yaml > zipkin-deployment.yaml
# edit zipkin-deployment.yaml to set the correct environment variables  and other configurations
kubectl apply -f zipkin-deployment.yaml

kubectl create service clusterip ms-zipkin --tcp=9412:9411 --dry-run=client -o yaml > zipkin-service.yaml
kubectl apply -f zipkin-servuce.yaml
```


