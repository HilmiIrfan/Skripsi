import React from "react";
import TypingCard from "@/components/TypingCard";
const About = () => {
  const cardContent = `
    <p>Halo Semuanya, Saya merupakan Doyatama... Creator Sistem Ujian berbasis Big Data。</p>
    <p>Jika menurut Anda proyek ini bermanfaat bagi Anda, harap hargai.</p>
    <p>Apresiasi Anda adalah kekuatan pendorong bagi saya untuk terus maju!</p>
  `;
  return (
    <div className="app-container">
      <TypingCard title="Pembuat App" source={cardContent} />
    </div>
  );
};

export default About;
