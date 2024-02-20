import React from "react"
import { About } from "./About"
import { Blog } from "./Blog"
import { Contact } from "./Contact"
import { Counter } from "./Counter"
import { Portfolio } from "./Portfolio"
import { Services } from "./Services"
import { Testimonials } from "./Testimonials"
import { Hero } from "./Hero"

 const Home = () => {
  return (
    <>
      <Hero />
      <About />
      <Services />
      <Counter />
      <Portfolio />
      <Testimonials />
      <Blog />
      <Contact />
    </>
  )
};
export default Home;
