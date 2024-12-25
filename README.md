# ForexAPI
註： 新版Spring Boot已不再支援Java 11，故使用Java 17
1. Run "docker build -t forex-api:latest ."
2. Run "docker compose up"
3. Run "curl --location 'localhost:8080/forex' \\\
   --header 'Content-Type: application/json' \\\
   --data '{
   "startDate": "2024/12/10",
   "endDate": "2024/12/22",
   "currency": "usd"
   }'"