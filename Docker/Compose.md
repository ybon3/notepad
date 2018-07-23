Reference: https://yeasy.gitbooks.io/docker_practice/compose/

配置範例：檔案名稱 `docker-compose.yml`

```yml
version: '3'
services:
  checkout:
    image: foo.bar.io/checkout-ms
    container_name: checkout-ms
    depends_on:
    - payment
    - order
    environment:
      - MS_ORDER_ADD_URL=http://localhost:8090/order/add
      - MS_PAYMENT_CHECKOUT_URL=http://localhost:8091/order/add
    ports:
      - "8080:8080"

  order:
    image: foo.bar.io/order-ms
    container_name: order-ms
    ports:
      - "8090:8090"

  payment:
    image: foo.bar.io/payment-ms
    container_name: payment-ms
    environment:
      - MS_ORDER_GETBYTRANSID_URL=http://localhost:8090/order/getByTransId
      - MS_ORDER_FILTER_URL=http://localhost:8090/order/filter
      - MS_ORDER_UPDATETRANID_URL=http://localhost:8090/order/updateTranId
      - MS_ORDER_UPDATEAUTH_URL=http://localhost:8090/order/updateAuth
    ports:
      - "8091:8091"
```

基本執行語法，於 `docker-compose.yml` 所在目錄下執行：

```console
docker-compose up

# Detached mode: Run containers in the background
docker-compose up -d
```


參數 `-p` 可以指定 projectName（預設是當前目錄名）

```console
docker-compose -p WTF up
```


簡略說明：

- 執行 `docker-compose up` 會建立一個 "projectName_default" 的 network，並供所有 `docker-compose.yml` 定義的 container 使用
- `services` 層下的每一個元素對應一個 container，在這邊稱做 serviceName
- 若未指定 `container_name`，則會將 container 命名為 "projectName_serviceName_1"
- `environment` 可以直接取代 `application.properties` 裡面的參數，名稱對應規則請自己理解
