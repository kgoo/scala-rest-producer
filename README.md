# scala-rest-producer
This project is a minimal REST API built with **Scala (Play Framework)** that receives JSON events and sends them to a **Kafka cluster**.

It includes:
- A REST API endpoint `/event` to POST key/value data
- Local Kafka cluster via Docker Compose (KRaft mode, no Zookeeper)
- Swagger UI for testing

---

## 🛠 Requirements

- [sbt](https://www.scala-sbt.org/)
- [Docker](https://www.docker.com/) and Docker Compose
- Java 17+

---

## 🚀 How to Run the Application

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/scala-rest-producer.git
cd scala-rest-producer
```

### 2. Start Kafka via Docker Compose

```bash
docker compose up -d
```

> This will start a Kafka cluster in [KRaft mode](https://kafka.apache.org/documentation/#kraft), with a Kafka CLI container for debugging.

### 3. Run the Play Application

```bash
sbt run
```

> The app will start at `http://localhost:9000`

---

## 🧪 How to Test the API

### Swagger UI

Visit:

```
http://localhost:9000/docs/index.html
```

This provides a visual interface to test the `/event` POST endpoint.

### Sample Request

```http
POST /event
Content-Type: application/json

{
  "key": "user123",
  "value": "some_data123"
}
```

The server will add the current timestamp and publish it to Kafka topic `events`.

---

## How to Consume from Kafka

Use the CLI container:

```bash
docker exec -it kafka-cli sh
kafka-console-consumer --bootstrap-server kafka:9092 --topic events --from-beginning
```

You should see something like:

```json
{"key":"user123","value":"some_data123","timestamp":1713001234567}
```

---

## ⚙️ Configuration

Kafka settings are in [`conf/application.conf`](conf/application.conf):

```hocon
kafka {
  bootstrap.servers = "localhost:9092"
  topic = "events"
}
```

---

## 📄 License

MIT
