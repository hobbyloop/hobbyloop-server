#!/bin/bash

db_config=$(aws ssm get-parameter --name /api-gateway-server/secret --with-decryption --query 'Parameter.Value' --output text)

echo "$db_config" > /home/ubuntu/app/config/application-secret.yml