import { Button } from "antd";
import TypingCard from "@/components/TypingCard";
import steps from "./steps";

const guide = function () {};
const Guide = function () {
  const cardContent = `引导页对于一些第一次进入项目的人很有用，你可以简单介绍下项目的功能。
                       本Demo是基于<a href="https://github.com/kamranahmedse/driver.js" target="_blank">driver.js</a>`;
  return (
    <div className="app-container">
      <TypingCard title="新手引导" source={cardContent} />
      <Button type="primary" onClick={guide}>
        打开引导
      </Button>
    </div>
  );
};

export default Guide;
