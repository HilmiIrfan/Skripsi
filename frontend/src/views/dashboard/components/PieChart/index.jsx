/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect, useRef } from "react";
import { useSelector } from "react-redux";
import echarts from "@/lib/echarts";
import { debounce } from "@/utils";

const PieChart = ({
  width = "100%",
  height = "300px",
  styles = {},
  className = "",
  chartData,
}) => {
  const [chart, setChart] = useState(null);
  const chartRef = useRef(null);
  const sidebarCollapsed = useSelector((state) => state.app.sidebarCollapsed);

  const setOptions = (chart) => {
    const animationDuration = 3000;
    chart.setOption({
      tooltip: {
        trigger: "item",
        formatter: "{a} <br/>{b} : {c} ({d}%)",
      },
      legend: {
        left: "center",
        bottom: "10",
        data: ["Industries", "Technology", "Forex", "Gold", "Forecasts"],
      },
      calculable: true,
      series: [
        {
          name: "WEEKLY WRITE ARTICLES",
          type: "pie",
          roseType: "radius",
          radius: [15, 95],
          center: ["50%", "38%"],
          data: [
            { value: 320, name: "Industries" },
            { value: 240, name: "Technology" },
            { value: 149, name: "Forex" },
            { value: 100, name: "Gold" },
            { value: 59, name: "Forecasts" },
          ],
          animationEasing: "cubicInOut",
          animationDuration,
        },
      ],
    });
  };

  const initChart = () => {
    if (!chartRef.current) return;
    const newChart = echarts.init(chartRef.current, "macarons");
    setChart(newChart);
    setOptions(newChart, chartData);
  };

  const resize = () => {
    if (chart) {
      debounce(chart.resize.bind(chart), 300)();
    }
  };

  useEffect(() => {
    debounce(initChart, 300)();
    window.addEventListener("resize", resize);

    return () => {
      if (chart) {
        window.removeEventListener("resize", resize);
        chart.dispose();
      }
    };
  }, []);

  useEffect(() => {
    if (chart) {
      resize();
    }
  }, [sidebarCollapsed]);

  useEffect(() => {
    if (chart && chartData) {
      debounce(initChart, 300)();
    }
  }, [chartData]);

  return (
    <div
      className={className}
      ref={chartRef}
      style={{
        ...styles,
        height,
        width,
      }}
    />
  );
};

export default PieChart;
