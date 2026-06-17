/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect, useRef } from "react";
import { useSelector } from "react-redux";
import echarts from "@/lib/echarts";
import { debounce } from "@/utils";

const BarChart = ({
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
        trigger: "axis",
        axisPointer: {
          type: "shadow",
        },
      },
      grid: {
        top: 10,
        left: "2%",
        right: "2%",
        bottom: "3%",
        containLabel: true,
      },
      xAxis: [
        {
          type: "category",
          data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          axisTick: {
            alignWithLabel: true,
          },
        },
      ],
      yAxis: [
        {
          type: "value",
          axisTick: {
            show: false,
          },
        },
      ],
      series: [
        {
          name: "pageA",
          type: "bar",
          stack: "vistors",
          barWidth: "60%",
          data: [79, 52, 200, 334, 390, 330, 220],
          animationDuration,
        },
        {
          name: "pageB",
          type: "bar",
          stack: "vistors",
          barWidth: "60%",
          data: [80, 52, 200, 334, 390, 330, 220],
          animationDuration,
        },
        {
          name: "pageC",
          type: "bar",
          stack: "vistors",
          barWidth: "60%",
          data: [30, 52, 200, 334, 390, 330, 220],
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

export default BarChart;
