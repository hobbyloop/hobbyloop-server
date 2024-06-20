#!/bin/bash
npm install pm2 -g
cd /home/ubuntu/app
npm install axios
pm2 start /home/ubuntu/app/start.json