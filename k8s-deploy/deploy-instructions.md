## Required Images:

- `bitnami/zipkin:latest`
- `binami/kafka:latest`
- `keycloak/keycloak:latest`
- `maildev/maildev:latest`
- `mongo:latest`
- `mysql:latest`

From branch `40-dockerize-microservices` with `mvn clean package -DskipTests spring-boot:build-image` from each
micro-services :

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


