/* eslint-disable no-unused-vars */
import React, { useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";
import echarts from "@/lib/echarts";
import { debounce } from "@/utils";

const LineChart = () => {
  const [chart, setChart] = useState(null);
  const chartRef = useRef(null);
  const sidebarCollapsed = useSelector((state) => state.app.sidebarCollapsed);
  const chartData = useSelector((state) => state.app.chartData);

  const width = "100%";
  const height = "350px";
  const styles = {};
  const className = "";

  const setOptions = (chart, { expectedData, actualData } = {}) => {
    chart.setOption({
      backgroundColor: "#fff",
      xAxis: {
        data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        boundaryGap: false,
        axisTick: {
          show: false,
        },
      },
      grid: {
        left: 10,
        right: 10,
        bottom: 10,
        top: 30,
        containLabel: true,
      },
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "cross",
        },
        padding: [5, 10],
      },
      yAxis: {
        axisTick: {
          show: false,
        },
      },
      legend: {
        data: ["expected", "actual"],
      },
      series: [
        {
          name: "expected",
          itemStyle: {
            normal: {
              color: "#FF005A",
              lineStyle: {
                color: "#FF005A",
                width: 2,
              },
            },
          },
          smooth: true,
          type: "line",
          data: expectedData,
          animationDuration: 2800,
          animationEasing: "cubicInOut",
        },
        {
          name: "actual",
          smooth: true,
          type: "line",
          itemStyle: {
            normal: {
              color: "#3888fa",
              lineStyle: {
                color: "#3888fa",
                width: 2,
              },
              areaStyle: {
                color: "#f3f8ff",
              },
            },
          },
          data: actualData,
          animationDuration: 2800,
          animationEasing: "quadraticOut",
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

  // Initialize chart
  useEffect(() => {
    debounce(initChart, 300)();
    window.addEventListener("resize", resize);
    return () => {
      if (chart) {
        window.removeEventListener("resize", resize);
        chart.dispose();
        setChart(null);
      }
    };
  }, []);

  // Handle sidebar collapse and chartData changes
  useEffect(() => {
    if (chart) {
      resize();
      if (chartData) {
        setOptions(chart, chartData);
      }
    }
  }, [sidebarCollapsed, chartData]);

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

export default LineChart;
