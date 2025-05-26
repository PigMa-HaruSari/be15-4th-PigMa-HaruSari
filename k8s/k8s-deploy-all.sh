#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
RESET='\033[0m'

run_kubectl() {
  echo -n "$1... "
  if eval "$2"; then
    echo -e "${GREEN}SUCCESS${RESET}"
  else
    echo -e "${RED}FAILED${RESET}"
  fi
}

echo -e "${GREEN}[0/4] Apply Secrets...${RESET}"
run_kubectl "Apply secret.yml" "kubectl apply -f ./env/secret.yml"
run_kubectl "Apply configmap.yml" "kubectl apply -f ./env/configmap.yml"

echo -e "${GREEN}[1/4] Create ConfigMap for DB Init SQL...${RESET}"
run_kubectl "Create mariadb-init-sql configmap" "kubectl create configmap mariadb-init-sql --from-file=./db/initdb/Harusari_DDL_20250522_v1.4.sql --from-file=./db/initdb/Harusari_Dummy_20250522_v1.4.sql --dry-run=client -o yaml | kubectl apply -f -"

echo -e "${GREEN}[2/4] Apply MariaDB...${RESET}"
run_kubectl "Apply pv-mariadb.yml" "kubectl apply -f ./db/pv-mariadb.yml"
run_kubectl "Apply mariadb-sts.yml" "kubectl apply -f ./db/mariadb-sts.yml"
run_kubectl "Apply mariadb-ser.yml" "kubectl apply -f ./db/mariadb-ser.yml"

echo -e "${GREEN}[3/4] Apply Redis & RabbitMQ...${RESET}"
run_kubectl "Apply pv-redis.yml" "kubectl apply -f ./redis/pv-redis.yml"
run_kubectl "Apply redis-sts.yml" "kubectl apply -f ./redis/redis-sts.yml"
run_kubectl "Apply redis-ser.yml" "kubectl apply -f ./redis/redis-ser.yml"
run_kubectl "Apply rabbitmq-dep.yml" "kubectl apply -f ./rabbitmq/rabbitmq-dep.yml"
run_kubectl "Apply rabbitmq-ser.yml" "kubectl apply -f ./rabbitmq/rabbitmq-ser.yml"

echo -e "${GREEN}[4/4] Apply Backend & Frontend & Ingress...${RESET}"
run_kubectl "Apply harusari-boot-dep.yml" "kubectl apply -f ./boot/harusari-boot-dep.yml"
run_kubectl "Apply harusari-boot-ser.yml" "kubectl apply -f ./boot/harusari-boot-ser.yml"
run_kubectl "Apply harusari-vue-dep.yml" "kubectl apply -f ./vue/harusari-vue-dep.yml"
run_kubectl "Apply harusari-vue-ser.yml" "kubectl apply -f ./vue/harusari-vue-ser.yml"
run_kubectl "Apply ingress.yml" "kubectl apply -f ./ingress/ingress.yml"

echo -e "${GREEN} All resources have been applied!${RESET}"