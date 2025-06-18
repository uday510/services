#!/bin/bash

tmux new-session -d -s services

# Start config-server
tmux rename-window -t services:0 'config-server'
tmux send-keys -t services:0 'cd ~/services/config-server && mvn spring-boot:run' C-m
sleep 10

# Start eureka-server
tmux new-window -t services -n 'eureka-server'
tmux send-keys -t services:1 'cd ~/services/eureka-server && mvn spring-boot:run' C-m
sleep 10

# Start accounts
tmux new-window -t services -n 'accounts'
tmux send-keys -t services:2 'cd ~/services/accounts && mvn spring-boot:run' C-m
sleep 5

# Start loans
tmux new-window -t services -n 'loans'
tmux send-keys -t services:3 'cd ~/services/loans && mvn spring-boot:run' C-m
sleep 5

# Start cards
tmux new-window -t services -n 'cards'
tmux send-keys -t services:4 'cd ~/services/cards && mvn spring-boot:run' C-m
sleep 5

# Start gateway-server
tmux new-window -t services -n 'gateway-server'
tmux send-keys -t services:5 'cd ~/services/gateway-server && mvn spring-boot:run' C-m

# Attach to the tmux session
tmux attach-session -t services