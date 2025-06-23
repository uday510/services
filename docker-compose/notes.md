docker-compose -f accounts-db.yml up -d
docker-compose -f loans-db.yml up -d
docker-compose -f cards-db.yml up -d
docker-compose -f rabbitmq.yml up -d


docker network create micro-network

docker network inspect micro-network

docker ps
docker inspect --format='{{json .State.Health}}' rabbitmq | jq



docker system prune -a --volumes


docker compose -f docker-compose.yml up -d


docker compose -f db.yml up -d

docker compose -f rabbitmq.yml up -d


docker compose -f auth-server.yml up -d

docker compose -f apache.kafka.yml up -d


# Create a topic.

docker exec kafka /opt/kafka/bin/kafka-topics.sh \
  --create \
  --topic test-topic \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1

  # List topics:

  docker exec kafka /opt/kafka/bin/kafka-topics.sh \
  --list \
  --bootstrap-server localhost:9092


  # start a producer

  docker exec -it kafka /opt/kafka/bin/kafka-console-producer.sh \
  --topic test-topic \
  --bootstrap-server localhost:9092


  # start a consumer

  docker exec -it kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --topic test-topic \
  --from-beginning \
  --bootstrap-server localhost:9092



  docker compose -f apache-kafka.yml down


rm -rf ./kafka-data && mkdir ./kafka-data && chmod 700 ./kafka-data


docker compose up --force-recreate