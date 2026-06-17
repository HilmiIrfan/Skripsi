/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect, useRef } from "react";
import { useSelector } from "react-redux";
import echarts from "@/lib/echarts";
import { debounce } from "@/utils";

const RaddarChart = ({
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
      radar: {
        radius: "66%",
        center: ["50%", "42%"],
        splitNumber: 8,
        splitArea: {
          areaStyle: {
            color: "rgba(127,95,132,.3)",
            opacity: 1,
            shadowBlur: 45,
            shadowColor: "rgba(0,0,0,.5)",
            shadowOffsetX: 0,
            shadowOffsetY: 15,
          },
        },
        indicator: [
          { name: "Sales", max: 10000 },
          { name: "Administration", max: 20000 },
          { name: "Information Techology", max: 20000 },
          { name: "Customer Support", max: 20000 },
          { name: "Development", max: 20000 },
          { name: "Marketing", max: 20000 },
        ],
      },
      legend: {
        left: "center",
        bottom: "10",
        data: ["Allocated Budget", "Expected Spending", "Actual Spending"],
      },
      series: [
        {
          type: "radar",
          symbolSize: 0,
          areaStyle: {
            normal: {
              shadowBlur: 13,
              shadowColor: "rgba(0,0,0,.2)",
              shadowOffsetX: 0,
              shadowOffsetY: 10,
              opacity: 1,
            },
          },
          data: [
            {
              value: [5000, 7000, 12000, 11000, 15000, 14000],
              name: "Allocated Budget",
            },
            {
              value: [4000, 9000, 15000, 15000, 13000, 11000],
              name: "Expected Spending",
            },
            {
              value: [5500, 11000, 12000, 15000, 12000, 12000],
              name: "Actual Spending",
            },
          ],
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

export default RaddarChart;
