from kafka import KafkaConsumer
import json
import os
from database import get_connection, create_table

KAFKA_BROKER = os.getenv("KAFKA_BROKER", "kafka:9092")

def start_consumer():
    create_table()

    consumer = KafkaConsumer(
        "order-events",
        bootstrap_servers=KAFKA_BROKER,
        value_deserializer=lambda x: json.loads(x.decode("utf-8")),
        auto_offset_reset="earliest",
        group_id="query-group"
    )

    for message in consumer:
        data = message.value

        conn = get_connection()
        cur = conn.cursor()

        cur.execute("""
            INSERT INTO orders (order_id, product_id, quantity, price)
            VALUES (%s, %s, %s, %s)
            ON CONFLICT (order_id) DO NOTHING
        """, (
            data["order_id"],
            data["product_id"],
            data["quantity"],
            data["price"]
        ))

        conn.commit()
        cur.close()
        conn.close()