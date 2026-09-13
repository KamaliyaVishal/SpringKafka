# SpringKafka

A Spring Boot microservices project demonstrating event-driven communication with **Apache Kafka**. The repository contains two services:

- **`user-service`** — produces events (e.g., user creation/update) to a Kafka topic.
- **`notification-service`** — consumes those events and reacts to them (e.g., sends a notification).

> Note: adjust the descriptions above/below to match the exact producer/consumer flow implemented in each service.

---

## Table of Contents

- [What is Kafka?](#what-is-kafka)
- [Kafka Architecture](#kafka-architecture)
- [When to Use (and Avoid) Kafka](#when-to-use-and-avoid-kafka)
- [Project Structure](#project-structure)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Running the Services](#running-the-services)
- [License](#license)

---

## What is Kafka?

**Apache Kafka** is an open-source distributed event streaming platform originally built at LinkedIn and now maintained by the Apache Software Foundation. It is used to publish, store, and process streams of records (events) in real time.

At its core, Kafka acts as a durable, high-throughput **message broker / event log** that decouples the services that produce data (producers) from the services that consume it (consumers). Instead of services calling each other directly (as in REST-based communication), they communicate asynchronously through Kafka **topics**.

Key characteristics:

- **High throughput** — can handle millions of messages per second.
- **Durability** — messages are persisted to disk and replicated across brokers.
- **Scalability** — topics are partitioned and can be scaled horizontally across a cluster.
- **Fault tolerance** — replication ensures no data loss even if a broker goes down.
- **Publish/subscribe + queue semantics** — supports both broadcast (multiple consumer groups) and load-balanced (single consumer group) consumption.

Common use cases: event-driven microservices, log aggregation, real-time analytics, activity tracking, stream processing, and decoupling services (as demonstrated in this repo between `user-service` and `notification-service`).

---

## Kafka Architecture

### 🌐 Interactive Visualisation
Explore the live architecture simulator on the [SoftwareMill Kafka Visualisation Tool](https://softwaremill.com/kafka-visualisation/).

[![Kafka Visualization Preview](path/to/your/screenshot.png)](https://softwaremill.com/kafka-visualisation/)
*Click the image above to open the interactive Kafka simulator.*



```
                     ┌─────────────────────────────┐
                     │         Kafka Cluster        │
                     │                              │
 Producer ──────────▶│   Broker 1   Broker 2  ...   │──────────▶ Consumer
(user-service)       │                              │        (notification-service)
                     │   Topic: user-events          │
                     │   ┌───────┬───────┬───────┐  │
                     │   │ P0    │ P1    │ P2    │  │
                     │   └───────┴───────┴───────┘  │
                     └─────────────────────────────┘
                                  │
                          Coordinated via
                       Zookeeper / KRaft
```

Core components:

| Component | Description |
|---|---|
| **Producer** | Publishes (writes) records to a Kafka topic. In this project, `user-service` acts as a producer. |
| **Consumer** | Subscribes to and reads records from a topic. `notification-service` acts as a consumer here. |
| **Broker** | A Kafka server that stores data and serves client requests. A cluster is made up of multiple brokers. |
| **Topic** | A named stream/category to which records are published (e.g., `user-events`). |
| **Partition** | Each topic is split into partitions, allowing data to be parallelized and distributed across brokers. Order is guaranteed only within a single partition. |
| **Offset** | A unique, sequential ID assigned to each record within a partition, used to track consumer position. |
| **Consumer Group** | A group of consumers that share the work of reading from a topic's partitions — each partition is read by only one consumer within the group. |
| **Replication** | Each partition can have replicas across brokers for fault tolerance; one replica is the "leader," others are "followers." |
| **Zookeeper / KRaft** | Coordinates broker metadata, leader election, and cluster state. Newer Kafka versions replace Zookeeper with Kafka's built-in **KRaft** consensus protocol. |

**Typical flow in this project:**

1. `user-service` performs an action (e.g., a new user is registered).
2. It publishes an event/message to a Kafka topic (producer).
3. Kafka durably stores the event across partitions/brokers.
4. `notification-service` (consumer) subscribes to that topic and processes the event — e.g., sends a welcome notification/email.

---

## When to Use (and Avoid) Kafka

### ✅ When to use Kafka

- **Event-driven microservices** — decoupling services so they don't call each other synchronously.
- **High-throughput data pipelines** — ingesting large volumes of logs, metrics, or clickstream data.
- **Real-time stream processing** — powering analytics, fraud detection, or monitoring dashboards.
- **Replayable event log** — you need consumers to be able to re-read historical events (e.g., for reprocessing or debugging).
- **Multiple independent consumers** — several services need to react to the same event without the producer knowing about them.
- **Buffering/back-pressure handling** — smoothing out spikes in traffic between fast producers and slower consumers.
- **Guaranteed ordering per key** — e.g., all events for a given user need to be processed in order.

### ❌ When to avoid Kafka

- **Simple, low-volume request/response needs** — if a direct REST/gRPC call or a lightweight queue (e.g., RabbitMQ) solves the problem, Kafka's operational overhead may not be worth it.
- **Small teams/projects without ops capacity** — running and monitoring a Kafka cluster (or managed service) adds real operational complexity (partition management, retention, monitoring, schema evolution).
- **Strict, immediate consistency requirements** — Kafka is asynchronous by nature; if you need synchronous, transactional guarantees across services, a message broker isn't the right tool on its own.
- **Very small, infrequent messaging needs** — the infrastructure cost/complexity isn't justified for occasional, low-volume messages.
- **Need for complex routing logic out of the box** — Kafka is a log, not a smart router; tools like RabbitMQ (exchanges/routing keys) may fit better for complex routing.
- **Tight latency budgets in the sub-millisecond range** — Kafka is fast, but not designed for the lowest possible latency use cases (e.g., some HFT systems).

---

## Project Structure

```
SpringKafka/
├── notification-service/   # Kafka consumer — listens for events and triggers notifications
├── user-service/           # Kafka producer — publishes user-related events
└── .idea/                  # IDE config (not relevant to running the project)
```

---

## Tech Stack

- Java
- Spring Boot
- Spring Kafka
- Apache Kafka
- Maven

---

## Getting Started

### Prerequisites

- JDK 17+ (or the version configured in each service's `pom.xml`)
- Maven
- A running Kafka broker (locally via Docker, or a managed Kafka service)

### 1. Start Kafka locally (example using Docker Compose)

```yaml
# docker-compose.yml
version: "3.8"
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

```bash
docker-compose up -d
```

### 2. Clone the repository

```bash
git clone https://github.com/KamaliyaVishal/SpringKafka.git
cd SpringKafka
```

---

## Running the Services

Each service is an independent Spring Boot application. Run them from their respective directories:

```bash
# Terminal 1 — start the producer
cd user-service
mvn spring-boot:run

# Terminal 2 — start the consumer
cd notification-service
mvn spring-boot:run
```

Once both services are running and connected to Kafka, actions in `user-service` will publish events that `notification-service` picks up and processes.

> Update the ports, endpoints, and topic names above to match your actual `application.yml`/`application.properties` configuration.

---

## License

No license specified. Add a `LICENSE` file to clarify how others may use this project.
