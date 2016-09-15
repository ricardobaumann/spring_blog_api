#!/bin/sh
docker run -p 5432:5432 --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres
docker build -t spring_blog_api .
docker run -p 8080:8080 -t spring_blog_api