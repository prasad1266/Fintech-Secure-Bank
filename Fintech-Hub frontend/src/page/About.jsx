import React from "react"
import { Heading } from "../common/Heading"
import { about } from "../data/dummydata"

export const About = () => {
  return (
    <>
      <section className='about'>
        <div className='container flex'>
          {about.map((val) => (
            <>
              <div className='left' data-aos='fade-down-right'>
                <img src={val.cover} alt='' />
              </div>
              <div className='right' data-aos='fade-down-left'>
                <Heading title='Welcome to Online Banking' />
                <p>{val.desc}</p>
                <p>{val.desc1}</p>
                <button>Register Admin</button>
                <button className='primaryBtn'>Login</button>
              </div>
            </>
          ))}
        </div>
      </section>
    </>
  )
}
