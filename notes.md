chmod +x build-and-push.sh
./build-and-push.sh


docker network create micro-network

docker compose -f loans-db.yml up -d

docker compose -f accounts-db.yml up -d


docker compose -f db.yml up -d