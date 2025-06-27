chmod +x build-and-push.sh
./build-and-push.sh

docker network ls
docker network create micro-network

docker compose -f loans-db.yml up -d

docker compose -f accounts-db.yml up -d


docker compose -f db.yml up -d


docker-compose -f auth-service.yml up -d

docker exec kafka /opt/kafka/bin/kafka-topics.sh \
  --list \
  --bootstrap-server localhost:9092


sudo rm -rf ./kafka-data

mkdir ./kafka-data && sudo chmod -R 777 ./kafka-data

docker container prune -f

docker compose up -d --force-recreate


for dir in accounts cards config-server eureka-server gateway-server loans message-queue; do echo "📦 Building $dir..."; (cd $dir && mvn compile jib:dockerBuild); done

docker images --format '{{.Repository}}:{{.Tag}}' | grep '^uday510/' | xargs -n1 docker push
