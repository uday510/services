docker-compose -f accounts-db.yml up -d
docker-compose -f loans-db.yml up -d
docker-compose -f cards-db.yml up -d
docker-compose -f rabbitmq.yml up -d



docker network inspect micro-network

docker ps
docker inspect --format='{{json .State.Health}}' rabbitmq | jq



docker system prune -a --volumes