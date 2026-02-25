from kafka import KafkaProducer
from kafka.errors import KafkaError
import json
import os

KAFKA_BROKER = os.getenv("KAFKA_BROKER", "kafka:9092")

producer = KafkaProducer(
    bootstrap_servers=KAFKA_BROKER,
    value_serializer=lambda v: json.dumps(v).encode("utf-8"),
    retries=5
)

def send_event(topic, data):
    try:
        future = producer.send(topic, data)
        future.get(timeout=10)
    except KafkaError as e:
        raise Exception(f"Kafka error: {str(e)}")