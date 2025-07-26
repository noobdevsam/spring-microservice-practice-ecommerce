## Required Images:

- `bitnami/zipkin:latest`
- `bitnami/kafka:latest`
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

For mysql-product:

```bash
kubectl create deployment ms-db-mysql-product --image=mysql:latest --dry-run=client -o yaml > mysql-product-deployment.yaml
# edit mysql-product-deployment.yaml to set the correct environment variables and other configurations
kubectl apply -f mysql-product-deployment.yaml

kubectl create service clusterip ms-db-mysql-product --tcp=3306:3306 --dry-run=client -o yaml > mysql-product-service.yaml
kubectl apply -f mysql-product-service.yaml
```

For mysql-order:

```bash
kubectl create deployment ms-db-mysql-order --image=mysql:latest --dry-run=client -o yaml > mysql-order-deployment.yaml
# edit mysql-order-deployment.yaml to set the correct environment variables and other configurations
kubectl apply -f mysql-order-deployment.yaml

kubectl create service clusterip ms-db-mysql-order --tcp=3307:3307 --dry-run=client -o yaml > mysql-order-service.yaml
kubectl apply -f mysql-order-service.yaml
```

For mysql-payment:

```bash
kubectl create deployment ms-db-mysql-payment --image=mysql:latest --dry-run=client -o yaml > mysql-payment-deployment.yaml
# edit mysql-payment-deployment.yaml to set the correct environment variables and other configurations
kubectl apply -f mysql-payment-deployment.yaml

kubectl create service clusterip ms-db-mysql-payment --tcp=3308:3308 --dry-run=client -o yaml > mysql-payment-service.yaml
kubectl apply -f mysql-payment-service.yaml
```

For keycloak:

```bash
kubectl create deployment ms-keycloak --image=keycloak/keycloak:latest --dry-run=client -o yaml > keycloak-deployment.yaml
# edit keycloak-deployment.yaml to set the correct environment variables and other configurations
kubectl apply -f keycloak-deployment.yaml

kubectl create service clusterip ms-keycloak --tcp=8088:8088 --dry-run=client -o yaml > keycloak-service.yaml
kubectl apply -f keycloak-service.yaml
```

For kafka:

```bash
kubectl create deployment ms-kafka --image=bitnami/kafka:latest --dry-run=client -o yaml > kafka-deployment.yaml
# edit kafka-deployment.yaml to set the correct environment variables and other configurations
kubectl apply -f kafka-deployment.yaml

kubectl create service clusterip ms-kafka --tcp=9092:9092 --dry-run=client -o yaml > kafka-service.yaml
kubectl apply -f kafka-service.yaml
```

For maildev:

```bash
kubectl create deployment ms-maildev --image=maildev/maildev:latest --dry-run=client -o yaml > maildev-deployment.yaml
# edit maildev-deployment.yaml to set the correct environment variables and other configurations
kubectl apply -f maildev-deployment.yaml

kubectl create service clusterip ms-maildev --tcp=1080:1080 --tcp=1025:1025 --dry-run=client -o yaml > maildev-service.yaml
kubectl apply -f maildev-service.yaml
```

For zipkin:

```bash
kubectl create deployment ms-zipkin --image=bitnami/zipkin:latest --dry-run=client -o yaml > zipkin-deployment.yaml
# edit zipkin-deployment.yaml to set the correct environment variables  and other configurations
kubectl apply -f zipkin-deployment.yaml

kubectl create service clusterip ms-zipkin --tcp=9412:9411 --dry-run=client -o yaml > zipkin-service.yaml
kubectl apply -f zipkin-servuce.yaml
```


