

### Install dependecias
mvn install

### Start app
mvn spring-boot:run

### A URL abaixo retorna os produtores com maior intervalo entre dois prêmios consecutivos, e também para os produtores que obteve dois prêmios mais rápido
http://localhost:8080/movie/producers

### Methods HTTP implementados
GET, PUT, POST, DELETE

GET : http://localhost:8080/movie/  
GET : http://localhost:8080/movie/id  
POST: http://localhost:8080/movie/  
PUT: http://localhost:8080/movie/  
DELETE : http://localhost:8080/movie/id  

### Body POST 
{
  "year":2021,
  "title":"Viúva Negra"
  "studios":"Marvel Studios",
  "producers":"Kevin Feige",
  "winner":"yes"
}