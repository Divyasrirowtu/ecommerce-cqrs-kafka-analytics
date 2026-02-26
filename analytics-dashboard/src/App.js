import React, { useEffect, useState } from "react";
import axios from "axios";

function App() {
  const [totalSales, setTotalSales] = useState(0);
  const [topProduct, setTopProduct] = useState("");
  const [lastFiveMin, setLastFiveMin] = useState(0);
  const [productSales, setProductSales] = useState(0);
  const [productName, setProductName] = useState("Laptop");

  const fetchData = async () => {
    try {
      const total = await axios.get("http://localhost:8082/analytics/total-sales");
      const top = await axios.get("http://localhost:8082/analytics/top-product");
      const window = await axios.get("http://localhost:8082/analytics/last-5-min-sales");
      const product = await axios.get(
        `http://localhost:8082/analytics/product-sales/${productName}`
      );

      setTotalSales(total.data);
      setTopProduct(top.data);
      setLastFiveMin(window.data);
      setProductSales(product.data);
    } catch (error) {
      console.error("Error fetching analytics", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, [productName]);

  return (
    <div style={{ padding: "40px", fontFamily: "Arial" }}>
      <h1>📊 Ecommerce Analytics Dashboard</h1>

      <h2>Total Sales: ₹{totalSales}</h2>
      <h2>Top Product: {topProduct}</h2>
      <h2>Last 5 Min Sales: ₹{lastFiveMin}</h2>

      <hr />

      <h3>Check Product Sales</h3>
      <input
        value={productName}
        onChange={(e) => setProductName(e.target.value)}
        placeholder="Enter product name"
      />
      <button onClick={fetchData}>Check</button>

      <h3>{productName} Sales: ₹{productSales}</h3>
    </div>
  );
}

export default App;