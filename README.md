for run: `docker-compose up`

Swagger URI: http://localhost:8080/swagger-ui/index.html#/

Why Mongo? More Flexible
It seems like you're considering switching to Elasticsearch because of its indexed search capabilities.


Points for improvement:
    Test container
    Enhanced integration testing

Find By Id:
```
curl --location --request GET 'http://localhost:8081/file/667ccf5d495054304cbcc080'
```

Search: 
```
curl --location --request GET 'http://localhost:8081/file/search' \
--header 'Content-Type: application/json' \
--data-raw '{
  "searchValue"  :"value",
  "page":1,
  "perPage":10
}'
```

upload

```
curl --location --request POST 'http://localhost:8081/file/upload' \
--form 'file=@"/path/to/file"'
```