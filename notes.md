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
mkdir ./kafka-data
sudo chown -R 1000:1000 ./kafka-data

docker container prune -f

docker compose up -d --force-recreate